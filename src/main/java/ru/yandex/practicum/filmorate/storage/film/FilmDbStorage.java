package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.Genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MPA.MPAStorage;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
@Slf4j
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MPAStorage mpaStorage;
    private final GenreStorage genreStorage;
    private final DirectorStorage directorStorage;

    @Override
    public Collection<Film> findAll() {
        String sql = "SELECT * FROM FILMS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }


    @Override
    public Film create(Film film) throws ValidationException {
        if (findAll().contains(film) || (!StringUtils.hasText(film.getName())) || (film.getDescription().length() > 200)
                || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))
                || (film.getDuration() < 0)) {
            log.debug("Oh, no. validation failed");
            throw new ValidationException("oh, something was wrong");
        } else {
            String sqlQuery = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASEDATE, DURATION, RATING_MPA_ID, RATE)" +
                    " VALUES (?, ?, ?, ?, ?, ?)";

            jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate()),
                    film.getDuration(), film.getMpa().getId(), film.getRate());
            setID(film);
            Set<Genre> set = Set.copyOf(film.getGenres());

            for (Director d : film.getDirector()) {
                directorStorage.addDirector(d.getId(), film.getId());
            }

            for (Genre g : set) {
                genreStorage.createGenre(g.getId(), film.getId());
            }
            mpaStorage.createMPA(film.getMpa(), film.getId());

        }
        log.debug("added: {}", film);
        return getFilm(film.getId()).get();
    }

    @Override
    public Film update(Film film) throws NotFoundParameterException {
        if ((!StringUtils.hasText(film.getName())) || (film.getDescription().length() > 200)
                || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))
                || (film.getDuration() < 0) || (film.getId() < 0)) {
            log.debug("Oh, no. validation failed");
            throw new NotFoundParameterException("oh, something was wrong");
        } else {
            String sqlQuery = "UPDATE FILMS SET name = ?, description = ?, releaseDate = ?, DURATION = ?, " +
                    " rating_mpa_ID = ?, RATE = ? WHERE FILM_ID = ?";

            jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate()),
                    film.getDuration(), film.getMpa().getId(), film.getRate(), film.getId());

            mpaStorage.createMPA(film.getMpa(), film.getId());

            genreStorage.removeGenre(film.getId());

            directorStorage.removeDirector(film.getId());

            for (Director d : film.getDirector()) {
                directorStorage.addDirector(d.getId(), film.getId());
            }

            Set<Genre> set = Set.copyOf(film.getGenres());

            for (Genre g : set) {
                genreStorage.createGenre(g.getId(), film.getId());
            }


            mpaStorage.createMPA(film.getMpa(), film.getId());

        }
        log.debug("updated: {}", film);
        return getFilm(film.getId()).get();
    }

    @Override
    public Optional<Film> getFilm(Integer id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS WHERE FILM_ID = ?", id);

        if (userRows.next()) {
            Film film = new Film(
                    userRows.getInt("FILM_ID"),
                    userRows.getString("name"),
                    userRows.getString("description"),
                    userRows.getDate("releaseDate"),
                    userRows.getInt("duration"),
                    getGenre(id),
                    mpaStorage.getMPA(userRows.getInt("RATING_MPA_ID")),
                    userRows.getInt("rate"),
                    getLikes(id),
                    directorStorage.getDirectors(id)
            );

            log.info("Найден пользователь: {} {}", film.getId(), film.getName());

            return Optional.of(film);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    @Override
    public void createLike(Integer filmID, Integer userID) {
        String sqlQuery = "insert into FILM_LIKES(FILM_ID, USER_ID) values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmID, userID);
    }

    @Override
    public void removeLike(Integer filmID, Integer userID) {
        String sqlQuery = "DELETE FROM FILM_LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, filmID, userID);
    }

    @Override
    public Collection<Film> findFilmsByDirector(Integer directorID, String sortBy) {
        String sort = "f.RELEASEDATE";
        if (sortBy.equals("likes")) {
            sort = "COUNT(FL.USER_ID)";
        }
        String sql = "SELECT * FROM FILMS_DIRECTORS FD join FILMS F on FD.FILM_ID = F.FILM_ID left join FILM_LIKES FL " +
                "on F.FILM_ID = FL.FILM_ID WHERE FD.DIRECTOR_ID = ? GROUP BY F.FILM_ID ORDER BY " + sort;
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), directorID);
    }

    public Film makeFilm(ResultSet rs) throws SQLException {
        return new Film(
                rs.getInt("FILM_ID"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("releaseDate"),
                rs.getInt("duration"),
                getGenre(rs.getInt("FILM_ID")),
                mpaStorage.getMPA(rs.getInt("rating_MPA_id")),
                rs.getInt("rate"),
                getLikes(rs.getInt("FILM_ID")),
                directorStorage.getDirectors(rs.getInt("FILM_ID"))
        );
    }

    private Collection<Genre> getGenre(Integer id) {
        String sql = "SELECT * FROM Film_genre join GENRE G2 on G2.GENRE_ID = FILM_GENRE.GENRE_ID WHERE FILM_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), id);
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"), rs.getString("NAME"));
    }

    private Collection<Integer> getLikes(Integer id) {
        String sql = "SELECT * FROM FILM_LIKES WHERE FILM_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeLikes(rs), id);
    }

    private Integer makeLikes(ResultSet rs) throws SQLException {
        return rs.getInt("USER_ID");
    }

    private void setID(Film film) {
        for (Film f : findAll()) {
            if (f.equals(film))
                film.setId(f.getId());
        }
    }
    @Override
    public boolean removeFilm(int filmId) {
        int affectedRows = jdbcTemplate.update("DELETE FROM films WHERE film_id = ?", filmId);
        return affectedRows != 0;
    }
}

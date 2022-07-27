package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.Event.EventStorage;
import ru.yandex.practicum.filmorate.storage.Genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MPA.MPAStorage;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
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
    private final EventStorage eventStorage;

    @Override
    public Collection<Film> findAll() {
        String sql = "SELECT * FROM FILMS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> make(rs));
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

            for (Director d : film.getDirectors()) {
                directorStorage.addToFilm(d, film);
            }

            for (Genre g : set) {
                genreStorage.create(g.getId(), film.getId());
            }
            mpaStorage.create(film.getMpa(), film.getId());

        }
        log.debug("added: {}", film);
        return get(film.getId()).orElseThrow();
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

            mpaStorage.create(film.getMpa(), film.getId());

            genreStorage.remove(film.getId());

            directorStorage.remove(film.getId());

            for (Director d : film.getDirectors()) {
                directorStorage.addToFilm(d, film);
            }

            film.setNullDirectors(directorStorage.getByFilm(film.getId()));

            Set<Genre> set = Set.copyOf(film.getGenres());

            for (Genre g : set) {
                genreStorage.create(g.getId(), film.getId());
            }

            film.setGenres(genreStorage.findByFilm(film.getId()));

            mpaStorage.create(film.getMpa(), film.getId());

        }
        log.debug("updated: {}", film);
        return film;
    }

    @Override
    public Optional<Film> get(Integer id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS WHERE FILM_ID = ?", id);

        if (userRows.next()) {
            Film film = new Film(
                    userRows.getInt("FILM_ID"),
                    userRows.getString("name"),
                    userRows.getString("description"),
                    userRows.getDate("releaseDate"),
                    userRows.getInt("duration"),
                    getGenre(id),
                    mpaStorage.get(userRows.getInt("RATING_MPA_ID")),
                    userRows.getInt("rate"),
                    getLikes(id),
                    directorStorage.getByFilm(id)
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
        eventStorage.add(userID, filmID, EventType.LIKE, EventOperations.ADD);
    }

    @Override
    public void removeLike(Integer filmID, Integer userID) {
        String sqlQuery = "DELETE FROM FILM_LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, filmID, userID);
        eventStorage.add(userID, filmID, EventType.LIKE, EventOperations.REMOVE);
    }

    @Override
    public Collection<Film> findByDirector(Integer directorID, String sortBy) throws NotFoundParameterException {
        Director director = new Director(directorID, "TEST");
        if (!directorStorage.findAll().contains(director)) {
            throw new NotFoundParameterException("BAD directorID");
        }
        String sort = "f.RELEASEDATE";
        if (sortBy.equals("likes")) {
            sort = "COUNT(FL.USER_ID)";
        }
        String sql = "SELECT * FROM FILMS_DIRECTORS FD join FILMS F on FD.FILM_ID = F.FILM_ID left join FILM_LIKES FL " +
                "on F.FILM_ID = FL.FILM_ID WHERE FD.DIRECTOR_ID = ? GROUP BY F.FILM_ID ORDER BY " + sort;
        return jdbcTemplate.query(sql, (rs, rowNum) -> make(rs), directorID);
    }

    @Override
    public List<Film> getCommon(Integer userId, Integer friendId) {
        String sql = "select *\n" +
                "from FILMS\n" +
                "where FILM_ID in (select FILM_ID\n" +
                "                  from ((select FILM_ID from FILM_LIKES where USER_ID = ?)\n" +
                "                        INTERSECT\n" +
                "                        (select FILM_ID from FILM_LIKES where USER_ID = ?)))";
        return jdbcTemplate.query(sql, (rs, rowNum) -> make(rs), userId, friendId);
    }

    @Override
    public boolean delete(Integer filmId) {
        int affectedRows = jdbcTemplate.update("DELETE FROM films WHERE film_id = ?", filmId);
        return affectedRows != 0;
    }

    @Override
    public Collection<Film> searchAnyway(String query, String type) {
        switch (type) {
            case ("director"):
                return searchByDirector(query);
            case ("title"):
                return searchByTitle(query);
            case "director,title":
                String row = "SELECT f.film_id " +
                        "FROM films f " +
                        "WHERE UPPER(f.name) LIKE UPPER('%'||?||'%') " +
                        "UNION ALL " +
                        "SELECT df.film_id " +
                        "FROM FILMS_DIRECTORS df " +
                        "JOIN directors d ON df.director_id=d.director_id " +
                        "WHERE UPPER(d.DIRECTOR_NAME) LIKE UPPER('%'||?||'%')";
                return jdbcTemplate.query(row, (rs, rowNum) -> make(rs), query, query);
            case "title,director":
                String rowS = "SELECT *, COUNT(DISTINCT l.user_id) AS L " +
                        "FROM FILMS AS F " +
                        "LEFT JOIN FILM_LIKES AS l ON F.FILM_ID = l.FILM_ID " +
                        "LEFT JOIN FILMS_DIRECTORS AS FD ON FD.FILM_ID = f.FILM_ID " +
                        "LEFT JOIN DIRECTORS AS D ON D.DIRECTOR_ID = FD.DIRECTOR_ID " +
                        "WHERE F.NAME ILIKE CONCAT('%', ?, '%') " +
                        "OR D.DIRECTOR_NAME ILIKE CONCAT('%', ?, '%') " +
                        "GROUP BY F.film_id " +
                        "ORDER BY L DESC;";
                return jdbcTemplate.query(rowS, (rs, rowNum) -> make(rs), query, query);
            default:
                return null;
        }
    }

    public Collection<Film> searchByDirector(String query) {
        String sql = "SELECT * " +
                "FROM DIRECTORS AS D " +
                "LEFT JOIN FILMS_DIRECTORS FD ON D.DIRECTOR_ID = FD.DIRECTOR_ID " +
                "LEFT JOIN FILMS F ON F.FILM_ID = FD.FILM_ID " +
                "LEFT JOIN FILM_LIKES FL ON F.FILM_ID = FL.FILM_ID " +
                "WHERE DIRECTOR_NAME ILIKE CONCAT('%', ?, '%') " +
                "GROUP BY FD.DIRECTOR_ID " +
                "ORDER BY COUNT(FL.USER_ID)";
        return jdbcTemplate.query(sql, (rs, rowNum) -> make(rs), query);
    }

    public Collection<Film> searchByTitle(String query) {
        final String row = "SELECT * " +
                "FROM FILMS AS F " +
                "LEFT JOIN FILM_LIKES FL ON F.FILM_ID = FL.FILM_ID " +
                "WHERE F.NAME ILIKE CONCAT('%', ?, '%')" +
                "ORDER BY COUNT(FL.USER_ID)";
        return jdbcTemplate.query(row, (rs, rowNum) -> make(rs), query);
    }

    public Film make(ResultSet rs) throws SQLException {
        return new Film(
                rs.getInt("FILM_ID"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("releaseDate"),
                rs.getInt("duration"),
                getGenre(rs.getInt("FILM_ID")),
                mpaStorage.get(rs.getInt("rating_MPA_id")),
                rs.getInt("rate"),
                getLikes(rs.getInt("FILM_ID")),
                directorStorage.getByFilm(rs.getInt("FILM_ID"))
        );
    }

    public Collection<Film> getPopularByGenreAndYear(Integer genreId, String year) {
        String condition;
        String sql = "select *\n" +
                "from FILMS\n" +
                "where FILM_ID in (select FILM_ID\n" +
                "                  from (select extract(YEAR from RELEASEDATE) as release_year, FILMS.FILM_ID, GENRE_ID\n" +
                "                        from FILMS\n" +
                "                                 join FILM_GENRE FG\n" +
                "                                      on FILMS.FILM_ID = FG.FILM_ID)\n";

        if (genreId != null && year != null) {
            condition = "where GENRE_ID = ?\n" +
                    "and release_year = ?)";
            sql = sql + condition;
            return jdbcTemplate.query(sql, (rs, rowNum) -> make(rs), genreId, year);
        } else if (genreId != null) {
            condition = "where GENRE_ID = ?)";
            sql = sql + condition;
            return jdbcTemplate.query(sql, (rs, rowNum) -> make(rs), genreId);
        } else {
            condition = "where release_year = ?)";
            sql = sql + condition;
            return jdbcTemplate.query(sql, (rs, rowNum) -> make(rs), year);
        }
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
}
package ru.yandex.practicum.filmorate.storage.Genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Genre> findAll() {
        String sql = "SELECT * FROM GENRE";
        return jdbcTemplate.query(sql, (rs, rowNum) -> make(rs));
    }

    @Override
    public Genre get(Integer id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM GENRE WHERE GENRE_ID = ?", id);
        if (userRows.next()) {
            return new Genre(id, userRows.getString("NAME"));
        }
        return null;
    }

    @Override
    public void create(Integer genreID, Integer filmID) {
        String sqlQuery = "INSERT INTO FILM_GENRE (GENRE_ID, FILM_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, genreID, filmID);
    }

    @Override
    public void remove(Integer filmID) {
        String sqlQuery = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmID);
    }

    @Override
    public Collection<Genre> findByFilm(Integer filmID) {
        String sql = "SELECT * FROM FILM_GENRE join GENRE G2 on G2.GENRE_ID = FILM_GENRE.GENRE_ID WHERE FILM_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> make(rs), filmID);
    }


    private Genre make(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"), rs.getString("NAME"));
    }

}
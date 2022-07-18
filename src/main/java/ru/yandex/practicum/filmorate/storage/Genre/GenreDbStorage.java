package ru.yandex.practicum.filmorate.storage.Genre;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
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
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public Genre getGenre(Integer id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM GENRE WHERE GENRE_ID = ?", id);
        if (userRows.next()) {
            return new Genre(id, userRows.getString("NAME"));
        }
        return null;
    }

    @Override
    public void createGenre(Integer genreID, Integer filmID) {
        String sqlQuery = "INSERT INTO FILM_GENRE (GENRE_ID, FILM_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, genreID, filmID);
    }

    @Override
    public void removeGenre(Integer filmID) {
        String sqlQuery = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmID);
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"), rs.getString("NAME"));
    }

}
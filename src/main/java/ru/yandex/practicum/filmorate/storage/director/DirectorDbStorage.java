package ru.yandex.practicum.filmorate.storage.director;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Repository
public class DirectorDbStorage implements DirectorStorage {

    private final JdbcTemplate jdbcTemplate;

    public DirectorDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Director> findAll() {
        String sql = "SELECT * FROM DIRECTORS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeDirectors(rs));
    }

    @Override
    public Director getDirector(Integer id) throws NotFoundParameterException {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM DIRECTORS WHERE DIRECTOR_ID = ?", id);
        if (userRows.next()) {
            return new Director(id, userRows.getString("DIRECTOR_NAME"));
        }
        throw new NotFoundParameterException("BAD ID");
    }

    @Override
    public void addDirector(Integer ID, Integer filmID) {
        String sqlQuery = "INSERT INTO FILMS_DIRECTORS (FILM_ID, DIRECTOR_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmID, ID);
    }

    @Override
    public void removeDirector(Integer filmID) {
        String sqlQuery = "DELETE FROM FILMS_DIRECTORS WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmID);
    }

    public Collection<Director> getDirectors(Integer id) {
        String sql = "SELECT * FROM FILMS_DIRECTORS FD left join DIRECTORS D on FD.DIRECTOR_ID = D.DIRECTOR_ID " +
                "WHERE FILM_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeDirectors(rs), id);
    }

    public Director makeDirectors(ResultSet rs) throws SQLException {
        return new Director(rs.getInt("DIRECTOR_ID"), rs.getString("DIRECTOR_NAME"));
    }

    @Override
    public Director updateDirector(Director director) {
        String sqlQuery = "UPDATE DIRECTORS SET DIRECTOR_NAME = ? WHERE DIRECTOR_ID = ?";
        jdbcTemplate.update(sqlQuery, director.getName(), director.getId());
        return director;
    }

    @Override
    public Director createDirector(Director director) {
        String sqlQuery = "INSERT INTO DIRECTORS (DIRECTOR_NAME) VALUES (?)";
        jdbcTemplate.update(sqlQuery, director.getName());
        director.setId(setID());
        return director;
    }

    @Override
    public void deleteDirector(Integer directorID) {
        String sqlQuery = "DELETE FROM DIRECTORS WHERE DIRECTOR_ID = ?";
        jdbcTemplate.update(sqlQuery, directorID);
    }

    private Integer setID() {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT COUNT(DIRECTOR_ID) AS ID FROM DIRECTORS ");
        if (userRows.next()) {
            return userRows.getInt("ID");
        } else return null;
    }
    }

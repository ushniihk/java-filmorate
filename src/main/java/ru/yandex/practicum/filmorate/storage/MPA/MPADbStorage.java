package ru.yandex.practicum.filmorate.storage.MPA;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Repository
@RequiredArgsConstructor
public class MPADbStorage implements MPAStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<MPA> findAll() {
        String sql = "SELECT * FROM RATING_MPA";
        return jdbcTemplate.query(sql, (rs, rowNum) -> make(rs));
    }

    @Override
    public MPA get(Integer id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM RATING_MPA WHERE RATING_MPA_ID = ?", id);
        if (userRows.next()) {
            return new MPA(id, userRows.getString("NAME"));
        }
        return null;
    }

    private MPA make(ResultSet rs) throws SQLException {
        return new MPA(rs.getInt("RATING_MPA_ID"), rs.getString("NAME"));
    }

    @Override
    public void create(MPA mpa, Integer film_id) {
        String sqlQuery = "UPDATE FILMS SET rating_mpa_ID = ? WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, mpa.getId(), film_id);
    }

}

package ru.yandex.practicum.filmorate.storage.MPA;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Repository
public class MPADbStorage implements MPAStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MPADbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Collection<MPA> findAll() {
        String sql = "SELECT * FROM RATING_MPA";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMPA(rs));
    }

    @Override
    public MPA getMPA(Integer id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM RATING_MPA WHERE RATING_MPA_ID = ?", id);
        if (userRows.next()) {
            return new MPA(id, userRows.getString("NAME"));
        }
        return null;
    }

    private MPA makeMPA(ResultSet rs) throws SQLException {
        return new MPA(rs.getInt("RATING_MPA_ID"), rs.getString("NAME"));
    }

    @Override
    public void createMPA(MPA mpa, Integer film_id) {
        String sqlQuery = "UPDATE FILM SET rating_mpa_ID = ? WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, mpa.getId(), film_id);
    }


}

package ru.yandex.practicum.filmorate.storage.Event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperations;
import ru.yandex.practicum.filmorate.model.EventType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Repository
@Slf4j
public class EventDaoImpl implements EventDao {

    private final JdbcTemplate jdbcTemplate;

    public EventDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Event> getEvents(Integer id) {

        String row = "SELECT * " +
                "FROM events " +
                "JOIN users ON events.user_id = users.user_id " +
                "JOIN friends ON users.user_id = friends.friend_id " +
                "WHERE friends.user_id  = ?";
        return jdbcTemplate.query(row, (rs, rowNum) -> makeEvent(rs), id);
    }

    @Override
    public void add(Object object, EventType eventType, EventOperations operation) {

    }

    //еще ивент маппинг
    private Event makeEvent(ResultSet rs) throws SQLException {
        return new Event(
                rs.getLong("time_stamp"),
                rs.getInt("user_id"),
                rs.getString("event_type"),
                rs.getString("event_operation"),
                rs.getInt("event_id"),
                rs.getLong("entity_id"));
    }
}

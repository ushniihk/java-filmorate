package ru.yandex.practicum.filmorate.storage.Event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperations;
import ru.yandex.practicum.filmorate.model.EventType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Collection;

@Slf4j
@Repository
public class EventDBStorage implements EventStorage {

    private final JdbcTemplate jdbcTemplate;

    public EventDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Event> getEvents(Integer id) {
        String row = "SELECT * " +
                "FROM events " +
                "JOIN users ON events.user_id = users.user_id " +
                "WHERE users.user_id  = ?";
        return jdbcTemplate.query(row, (rs, rowNum) -> make(rs), id);
    }

    @Override
    public void add(Integer userId, Integer entityId, EventType eventType, EventOperations operation) {
        String query = "INSERT INTO events (user_id, event_type, event_operation, time_stamp, entity_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(query, userId, eventType.toString(), operation.toString(), Instant.now().toEpochMilli(), entityId);
    }

    private Event make(ResultSet rs) throws SQLException {
        return new Event(
                rs.getLong("time_stamp"),
                rs.getInt("user_id"),
                rs.getInt("event_id"),
                rs.getLong("entity_id"),
                rs.getString("event_type"),
                rs.getString("event_operation"));
    }
}


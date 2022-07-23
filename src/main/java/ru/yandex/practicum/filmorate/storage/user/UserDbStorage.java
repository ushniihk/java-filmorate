package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exceptions.CreatingException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.exceptions.UpdateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;


@Repository
@Slf4j
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmStorage filmStorage;

    @Override
    public Collection<User> findAll() {
        String sql = "SELECT * FROM USERS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User create(User user) throws CreatingException {
        if (findAll().contains(user) || (!StringUtils.hasText(user.getEmail())) || (!user.getEmail().contains("@"))
                || (!StringUtils.hasText(user.getLogin())) || (user.getLogin().contains(" "))
                || user.getBirthday().isAfter(LocalDate.now())
        ) {
            log.debug("Oh, no. validation failed");
            throw new CreatingException("oh, something was wrong");
        } else {
            if (!StringUtils.hasText(user.getName())) {
                user.setName(user.getLogin());
            }
            String sqlQuery = "INSERT INTO USERS (email, login, name, birthday) VALUES (?, ?, ?, ?)";

            jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(),
                    Date.valueOf(user.getBirthday()));
            setID(user);

            createFriends(user);

            log.debug("added: {}", user);
        }
        return user;
    }

    @Override
    public boolean deleteUser(Integer userId) {
        String sqlQuery = "DELETE FROM users WHERE user_id = ?";
        int rowsAffected = jdbcTemplate.update(sqlQuery, userId);
        return rowsAffected != 0;
    }

    @Override
    public User update(User user) throws UpdateException {
        if ((!StringUtils.hasText(user.getEmail())) || (!user.getEmail().contains("@"))
                || (!StringUtils.hasText(user.getLogin())) || user.getLogin().contains(" ")
                || user.getBirthday().isAfter(LocalDate.now()) || (user.getId() < 0)) {
            log.debug("Oh, no. validation failed");
            throw new UpdateException("oh, something was wrong");
        } else {
            String sqlQuery = "update users set EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? where user_id = ?";

            jdbcTemplate.update(sqlQuery,
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    Date.valueOf(user.getBirthday()),
                    user.getId()
            );
            String sqlQuery2 = "delete from friends where user_id = ?";
            jdbcTemplate.update(sqlQuery2, user.getId());
            createFriends(user);

        }
        return user;
    }

    @Override
    public Optional<User> getUser(Integer id) throws NotFoundParameterException {

        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE USER_ID = ?", id);

        if (userRows.next()) {
            User user = new User(
                    userRows.getInt("USER_ID"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    getFriendsByUser(id),
                    userRows.getString("name"),
                    userRows.getDate("birthday")
            );
            log.info("Найден пользователь: {} {}", user.getId(), user.getName());
            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    @Override
    public Collection<User> showAllFriends(Integer id) throws NotFoundParameterException {
        Collection<User> users = new ArrayList<>();
        if (getUser(id).isPresent()) {
            for (Integer user_id : getUser(id).get().getFriends()) {
                if (getUser(user_id).isPresent())
                    users.add(getUser(user_id).get());
            }
        } else {
            throw new NotFoundParameterException("bad id");
        }
        return users;
    }

    @Override
    public Collection<User> showCommonFriends(Integer id, Integer otherId) throws NotFoundParameterException {
        String sql = "SELECT * FROM users WHERE user_id IN (SELECT friend_id FROM friends WHERE user_id = ?) " +
                "AND user_id IN (SELECT friend_id FROM friends WHERE user_id = ?) ORDER BY USER_ID;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id, otherId);
    }

    @Override
    public void addFriend(Integer id, Integer friendId) {
        String sqlQuery2 = "INSERT INTO FRIENDS (USER_ID, FRIEND_ID, FRIENDSHIP) VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlQuery2, id, friendId, 1);
    }

    @Override
    public void deleteFriend(Integer id, Integer friendId) {
        String sqlQuery2 = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery2, id, friendId);
    }

    @Override
    public Map<Integer, Collection<Integer>> getUsersAndLikes() {
        Map<Integer, Collection<Integer>> usersLikes = new HashMap<>();
        for (User user : findAll()) {
            usersLikes.put(user.getId(), getLikedFilmsByUser(user.getId()));
        }
        return usersLikes;
    }

    @Override
    public int getUserIdWithCommonLikes(int id) {
        Collection<Integer> usersLikes = getUsersAndLikes().get(id);
        long count = 0;
        int userID = -1;
        for (Integer i : getUsersAndLikes().keySet()) {
            if (id != i) {
                long l = getUsersAndLikes().get(i).stream().filter(usersLikes::contains).count();
                if (l > count) {
                    count = l;
                    userID = i;
                }
            }
        }
        return userID;
    }

    @Override
    public Collection<Integer> getFilmsIdByRecommendations(int id) {
        Collection<Integer> films = getUsersAndLikes().get(getUserIdWithCommonLikes(id));
        films.removeAll(getUsersAndLikes().get(id));
        return films;
    }

    @Override
    public Collection<Film> getFilmsByRecommendations(int id) throws NotFoundParameterException {
        Collection<Film> recommendations = new ArrayList<>();
        for (Integer i : getFilmsIdByRecommendations(id)) {
            Optional<Film> film = filmStorage.getFilm(i);
            film.ifPresent(recommendations::add);
        }
        return recommendations;
    }

    private Collection<Integer> getFriendsByUser(Integer id) {
        String sql = "SELECT friend_id FROM FRIENDS WHERE USER_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFriends(rs), id);
    }

    private Integer makeFriends(ResultSet rs) throws SQLException {
        return rs.getInt("friend_id");
    }

    private User makeUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("USER_ID"),
                rs.getString("email"),
                rs.getString("login"),
                getFriendsByUser(rs.getInt("USER_ID")),
                rs.getString("name"),
                rs.getDate("birthday"));
    }

    private void setID(User user) {
        for (User u : findAll()) {
            if (u.equals(user))
                user.setId(u.getId());
        }
    }

    private void createFriends(User user) {
        for (Integer i : user.getFriends()) {
            String sqlQuery2 = "INSERT INTO FRIENDS (USER_ID, FRIEND_ID, FRIENDSHIP) VALUES (?, ?, ?)";
            jdbcTemplate.update(sqlQuery2, user.getId(), i, 1);
        }
    }

    private Collection<Integer> getLikedFilmsByUser(Integer id) {
        String sql = "SELECT FILM_ID FROM FILM_LIKES WHERE USER_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("FILM_ID"), id);
    }

}

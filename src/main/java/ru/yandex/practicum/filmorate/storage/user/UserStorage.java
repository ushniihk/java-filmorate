package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exceptions.CreatingException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.exceptions.UpdateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface UserStorage {
    Collection<User> findAll();

    User create(User user) throws CreatingException;

    User update(User user) throws UpdateException;

    boolean deleteUser(Integer userId);

    Optional<User> getUser(Integer id) throws NotFoundParameterException;

    Collection<User> showAllFriends(Integer id) throws NotFoundParameterException;

    Collection<User> showCommonFriends(Integer id, Integer otherId) throws NotFoundParameterException;

    void addFriend(Integer id, Integer friendId) throws NotFoundParameterException, UpdateException;

    void deleteFriend(Integer id, Integer friendId) throws NotFoundParameterException, UpdateException;

    Collection<Film> getFilmsByRecommendations(int id) throws NotFoundParameterException;

    Map<Integer, Collection<Integer>> getUsersAndLikes();

    int getUserIdWithCommonLikes(int id);

    Collection<Integer> getFilmsIdByRecommendations(int id);
}


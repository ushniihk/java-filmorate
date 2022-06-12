package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exceptions.CreatingException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.exceptions.UpdateException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> findAll();

    User create(User user) throws CreatingException;

    User update(User user) throws UpdateException;

    User getUser(Integer id) throws NotFoundParameterException;

    Collection<User> showAllFriends(Integer id);

    Collection<User> showCommonFriends(Integer id, Integer otherId);

}


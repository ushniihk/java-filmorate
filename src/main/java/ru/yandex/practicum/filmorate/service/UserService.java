package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.CreatingException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.exceptions.UpdateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public void addFriend(Integer id, Integer friendId) throws NotFoundParameterException {
        userStorage.getUser(id).addFriend(userStorage.getUser(friendId).getId());
        userStorage.getUser(friendId).addFriend(userStorage.getUser(id).getId());
    }

    public void deleteFriend(Integer id, Integer friendId) throws NotFoundParameterException {
        userStorage.getUser(id).removeFriend(userStorage.getUser(friendId).getId());
        userStorage.getUser(friendId).removeFriend(userStorage.getUser(id).getId());
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) throws CreatingException {
        return userStorage.create(user);
    }

    public User update(User user) throws UpdateException {
        return userStorage.update(user);
    }

    public User getUser(Integer id) throws NotFoundParameterException {
        return userStorage.getUser(id);
    }

    public Collection<User> showAllFriends(Integer id) {
        return userStorage.showAllFriends(id);
    }

    public Collection<User> showCommonFriends(Integer id, Integer otherId) {
        return userStorage.showCommonFriends(id, otherId);
    }


}

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

    public void addFriend(Integer id, Integer friendId) throws NotFoundParameterException, UpdateException {
        if (checkID(id))
            throw new NotFoundParameterException("bad id");
        if (checkID(friendId))
            throw new NotFoundParameterException("bad id");
        userStorage.addFriend(id, friendId);
    }

    public void deleteFriend(Integer id, Integer friendId) throws NotFoundParameterException, UpdateException {
        if (checkID(id))
            throw new NotFoundParameterException("bad id");
        if (checkID(friendId))
            throw new NotFoundParameterException("bad id");
        userStorage.deleteFriend(id, friendId);
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

    public void deleteUser(Integer userId) throws NotFoundParameterException {
        if (checkID(userId)) {
            throw new NotFoundParameterException("bad id");
        }
        boolean deleted = userStorage.deleteUser(userId);
        if (!deleted) {
            throw new NotFoundParameterException("No User With Such Id");
        }
        userStorage.deleteUser(userId);
    }

    public User getUser(Integer id) throws NotFoundParameterException {
        if (checkID(id))
            throw new NotFoundParameterException("bad id");
        return userStorage.getUser(id).orElseThrow(() -> new NotFoundParameterException("bad id"));
    }

    public Collection<User> showAllFriends(Integer id) throws NotFoundParameterException {
        if (checkID(id))
            throw new NotFoundParameterException("bad id");
        return userStorage.showAllFriends(id);
    }

    public Collection<User> showCommonFriends(Integer id, Integer otherId) throws NotFoundParameterException {
        if (checkID(id))
            throw new NotFoundParameterException("bad id");
        if (checkID(otherId))
            throw new NotFoundParameterException("bad id");
        return userStorage.showCommonFriends(id, otherId);
    }

    private boolean checkID(Integer id) {
        return (id == null || id < 0);
    }

}

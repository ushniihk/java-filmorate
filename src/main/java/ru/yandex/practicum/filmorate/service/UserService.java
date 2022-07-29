package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.CreatingException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.exceptions.UpdateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public void addFriend(Integer id, Integer friendId) throws NotFoundParameterException, UpdateException {
        if (checkID(id) || checkID(friendId))
            throw new NotFoundParameterException("bad id");
        userStorage.addFriend(id, friendId);
    }

    public void deleteFriend(Integer id, Integer friendId) throws NotFoundParameterException, UpdateException {
        if (checkID(id) || checkID(friendId))
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

    public void delete(Integer userId) throws NotFoundParameterException {
        if (checkID(userId)) {
            throw new NotFoundParameterException("bad id");
        }
        boolean deleted = userStorage.delete(userId);
        if (!deleted) {
            throw new NotFoundParameterException("No User With Such Id");
        }
        userStorage.delete(userId);
    }

    public User get(Integer id) throws NotFoundParameterException {
        if (checkID(id))
            throw new NotFoundParameterException("bad id");
        return userStorage.get(id).orElseThrow(() -> new NotFoundParameterException("No User With Such Id"));
    }

    public Collection<User> showAllFriends(Integer id) throws NotFoundParameterException {
        if (checkID(id))
            throw new NotFoundParameterException("bad id");
        return userStorage.showAllFriends(id);
    }

    public Collection<User> showCommonFriends(Integer id, Integer otherId) throws NotFoundParameterException {
        if (checkID(id) || checkID(otherId))
            throw new NotFoundParameterException("bad id");
        return userStorage.showCommonFriends(id, otherId);
    }

    public Collection<Film> getFilmsByRecommendations(Integer id) throws NotFoundParameterException {
        if (checkID(id))
            throw new NotFoundParameterException("bad id");
        if (!hasLike(id) || !hasCommonLikes(id) || !hasFilmsByRecommendations(id))
            return new ArrayList<>();
        return userStorage.getFilmsByRecommendations(id);
    }

    private boolean checkID(Integer id) {
        return (id == null || id < 0);
    }

    private boolean hasLike(Integer id) {
        Collection<Integer>collection = userStorage.getLikedFilms(id);
        return collection.size() != 0;
    }

    private boolean hasCommonLikes(Integer id) {
        return userStorage.getIdWithCommonLikes(id) != -1;
    }

    private boolean hasFilmsByRecommendations(Integer id) {
        return userStorage.getFilmsIdByRecommendations(id).size() != 0;
    }

}

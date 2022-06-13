package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final ConcurrentMap<Integer, User> users = new ConcurrentHashMap();
    private int counterID = 1;

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User create(User user) throws CreatingException {
        if (users.containsValue(user) || (!StringUtils.hasText(user.getEmail())) || (!user.getEmail().contains("@"))
                || (!StringUtils.hasText(user.getLogin())) || (user.getLogin().contains(" "))
                || user.getBirthday().isAfter(LocalDate.now())
        ) {
            log.debug("Oh, no. validation failed");
            throw new CreatingException("oh, something was wrong");
        } else {
            if (!StringUtils.hasText(user.getName())) {
                user.setName(user.getLogin());
            }
            user.setId(counterID++);
            users.put(user.getId(), user);
            log.debug("added: {}", user.toString());
        }
        return user;
    }

    @Override
    public User update(User user) throws UpdateException {
        if ((!StringUtils.hasText(user.getEmail())) || (!user.getEmail().contains("@"))
                || (!StringUtils.hasText(user.getLogin())) || user.getLogin().contains(" ")
                || user.getBirthday().isAfter(LocalDate.now()) || (user.getId() < 0)) {
            log.debug("Oh, no. validation failed");
            throw new UpdateException("oh, something was wrong");
        } else {
            users.put(user.getId(), user);
            log.debug("updated: {}", user.toString());
        }
        return user;
    }

    @Override
    public User getUser(Integer id) throws NotFoundParameterException {
        if (checkID(id))
            throw new NotFoundParameterException("bad id");
        return users.get(id);
    }

    @Override
    public Collection<User> showAllFriends(Integer id) {
        List<User> list = new ArrayList<>();
        users.get(id).getFriends().forEach(i -> list.add(users.get(i)));
        return list;
    }

    @Override
    public Collection<User> showCommonFriends(Integer id, Integer otherId) {
        List<User> list = new ArrayList<>();
        users.get(id).getFriends().stream()
                .filter(users.get(otherId).getFriends()::contains)
                .forEach(i -> list.add(users.get(i)));
        return list;
    }

    private boolean checkID(Integer id) {
        return (id == null || id < 0);
    }

}

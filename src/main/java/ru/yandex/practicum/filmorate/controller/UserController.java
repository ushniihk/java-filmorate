package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private ConcurrentMap<Integer, User> users = new ConcurrentHashMap();
    private int counterID = 1;

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
            if (users.containsValue(user) || (!StringUtils.hasText(user.getEmail())) || (!user.getEmail().contains("@"))
                    || (!StringUtils.hasText(user.getLogin())) || (user.getLogin().contains(" "))
                    || user.getBirthday().isAfter(LocalDate.now())
            ) {
                log.debug("Oh, no. validation failed");
                throw new ValidationException("oh, something was wrong");
            } else {
                if ((user.getName() == null) || user.getName().isEmpty()) {
                    user.setName(user.getLogin());
                }
                user.setId(counterID);
                users.put(user.getId(), user);
                log.debug("added: {}", user.toString());
                counterID++;
            }

        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) throws ValidationException {
            if ((!StringUtils.hasText(user.getEmail())) || (!user.getEmail().contains("@"))
                    || (!StringUtils.hasText(user.getLogin())) || user.getLogin().contains(" ")
                    || user.getBirthday().isAfter(LocalDate.now()) || (user.getId() < 0)) {
                log.debug("Oh, no. validation failed");
                throw new ValidationException("oh, something was wrong");
            } else {
                users.put(user.getId(), user);
                log.debug("updated: {}", user.toString());
            }

        return user;
    }
}

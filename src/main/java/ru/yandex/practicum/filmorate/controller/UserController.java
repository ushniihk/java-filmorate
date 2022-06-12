package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    UserStorage userStorage;
    UserService userService;

    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    @PostMapping
    public User create(@RequestBody User user) throws CreatingException {
        return userStorage.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) throws UpdateException {
        return userStorage.update(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id) throws NotFoundParameterException {
        return userStorage.getUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Integer id, @PathVariable Integer friendId) throws NotFoundParameterException {
        return userStorage.getUser(userService.addFriend(userStorage.getUser(id), userStorage.getUser(friendId)));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) throws NotFoundParameterException {
        return userStorage.getUser(userService.deleteFriend(userStorage.getUser(id), userStorage.getUser(friendId)));
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable Integer id) {
        return userStorage.showAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> showCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        return userStorage.showCommonFriends(id, otherId);
    }


}

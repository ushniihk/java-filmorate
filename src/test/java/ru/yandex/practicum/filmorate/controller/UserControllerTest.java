package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.CreatingException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.exceptions.UpdateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Test
    void shouldCreateNewUser() throws CreatingException {
        UserController userController = new UserController(new UserService(new InMemoryUserStorage()));
        User user = new User("mail@mail.ru", "login", LocalDate.parse("2022-01-27"));
        userController.create(user);
        assertTrue(userController.findAll().contains(user));
        assertEquals(user.getName(), (user.getLogin()));
    }

    @Test
    void shouldNotCreateBecauseEmailIsInvalid() {
        UserController userController = new UserController(new UserService(new InMemoryUserStorage()));
        User user = new User("mail-mail.ru", "login", LocalDate.parse("2022-01-27"));
        final CreatingException exception = assertThrows(
                CreatingException.class,
                () -> userController.create(user));
        assertEquals("oh, something was wrong", exception.getMessage());

    }

    @Test
    void shouldNotCreateBecauseUserIsAlreadyExist() throws CreatingException {
        UserController userController = new UserController(new UserService(new InMemoryUserStorage()));
        User user = new User("mail@mail.ru", "login", LocalDate.parse("2022-01-27"));
        User user1 = new User("mail@mail.ru", "login", LocalDate.parse("2022-01-27"));
        userController.create(user);
        final CreatingException exception = assertThrows(
                CreatingException.class,
                () -> userController.create(user1));
        assertEquals("oh, something was wrong", exception.getMessage());

    }

    @Test
    void shouldNotCreateBecauseLoginIsInvalid() {
        UserController userController = new UserController(new UserService(new InMemoryUserStorage()));
        User user2 = new User("mail@mail.ru", "log in", LocalDate.parse("2022-01-27"));
        final CreatingException exception = assertThrows(
                CreatingException.class,
                () -> userController.create(user2));
        assertEquals("oh, something was wrong", exception.getMessage());
    }

    @Test
    void shouldNotCreateBecauseLoginIsEmpty() {
        UserController userController = new UserController(new UserService(new InMemoryUserStorage()));
        User user = new User("mail@mail.ru", "", LocalDate.parse("2022-01-27"));
        final CreatingException exception = assertThrows(
                CreatingException.class,
                () -> userController.create(user));
        assertEquals("oh, something was wrong", exception.getMessage());
    }

    @Test
    void shouldNotCreateBecauseBirthDayIsInFuture() {
        UserController userController = new UserController(new UserService(new InMemoryUserStorage()));
        User user = new User("mail@mail.ru", "login", LocalDate.parse("2023-01-27"));
        final CreatingException exception = assertThrows(
                CreatingException.class,
                () -> userController.create(user));
        assertEquals("oh, something was wrong", exception.getMessage());
    }

    @Test
    void shouldUpdate() throws CreatingException, UpdateException {
        UserController userController = new UserController(new UserService(new InMemoryUserStorage()));
        User user = new User("mail@mail.ru", "login", LocalDate.parse("2022-01-27"));
        User user2 = new User("mail@mail.ru", "login2", LocalDate.parse("2022-01-27"));
        user2.setId(1);
        userController.create(user);
        userController.update(user2);
        assertTrue(userController.findAll().contains(user2));
        assertFalse(userController.findAll().contains(user));
    }

    @Test
    void shouldGetUser() throws CreatingException, NotFoundParameterException {
        UserController userController = new UserController(new UserService(new InMemoryUserStorage()));
        User user = new User("mail@mail.ru", "login", LocalDate.parse("2022-01-27"));
        User user2 = new User("mail@mail.ru", "login2", LocalDate.parse("2022-01-27"));
        userController.create(user);
        userController.create(user2);
        assertEquals(userController.getUser(2), user2);
        assertEquals(userController.getUser(1), user);
    }

    @Test
    void shouldAddFriend() throws NotFoundParameterException, CreatingException {
        UserController userController = new UserController(new UserService(new InMemoryUserStorage()));
        User user = new User("mail@mail.ru", "login", LocalDate.parse("2022-01-27"));
        User user2 = new User("mail@mail.ru", "login2", LocalDate.parse("2022-01-27"));
        User user3 = new User("mail@mail.ru", "login3", LocalDate.parse("2022-01-27"));
        userController.create(user);
        userController.create(user2);
        userController.create(user3);
        userController.addFriend(1, 3);
        assertTrue(user.getFriends().contains(user3.getId()));
        assertTrue(user3.getFriends().contains(user.getId()));
        assertFalse(user3.getFriends().contains(user2.getId()));
        assertFalse(user2.getFriends().contains(user.getId()));
    }

    @Test
    void shouldDeleteFriend() throws NotFoundParameterException, CreatingException {
        UserController userController = new UserController(new UserService(new InMemoryUserStorage()));
        User user = new User("mail@mail.ru", "login", LocalDate.parse("2022-01-27"));
        User user2 = new User("mail@mail.ru", "login2", LocalDate.parse("2022-01-27"));
        userController.create(user);
        userController.create(user2);
        userController.addFriend(1, 2);
        userController.deleteFriend(1, 2);
        assertFalse(user.getFriends().contains(user2.getId()));
        assertFalse(user2.getFriends().contains(user.getId()));
    }

    @Test
    void shouldShowAllFriends() throws CreatingException, NotFoundParameterException {
        UserController userController = new UserController(new UserService(new InMemoryUserStorage()));
        User user = new User("mail@mail.ru", "login", LocalDate.parse("2022-01-27"));
        User user2 = new User("mail@mail.ru", "login2", LocalDate.parse("2022-01-27"));
        userController.create(user);
        userController.create(user2);
        userController.addFriend(1, 2);
        assertEquals(userController.getFriends(1).size(), 1);
    }

    @Test
    void shouldShowCommonFriends() throws CreatingException, NotFoundParameterException {
        UserController userController = new UserController(new UserService(new InMemoryUserStorage()));
        User user = new User("mail@mail.ru", "login", LocalDate.parse("2022-01-27"));
        User user2 = new User("mail@mail.ru", "login2", LocalDate.parse("2022-01-27"));
        User user3 = new User("mail@mail.ru", "login3", LocalDate.parse("2022-01-27"));
        userController.create(user);
        userController.create(user2);
        userController.create(user3);
        userController.addFriend(1, 2);
        userController.addFriend(2, 3);
        userController.addFriend(1, 3);
        assertEquals(userController.showCommonFriends(1, 2).size(), 1);
        assertTrue(userController.showCommonFriends(1, 2).contains(user3));
    }
}
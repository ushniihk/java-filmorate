package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Test
    void shouldCreateNewUser() throws ValidationException {
        UserController userController = new UserController();
        User user = new User("mail@mail.ru", "login", LocalDate.parse("2022-01-27"));
        userController.create(user);
        assertTrue(userController.findAll().contains(user));
        assertEquals(user.getName(), (user.getLogin()));
    }

    @Test
    void shouldNotCreateBecauseEmailIsInvalid() {
        UserController userController = new UserController();
        User user = new User("mail-mail.ru", "login", LocalDate.parse("2022-01-27"));
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.create(user));
        assertEquals("oh, something was wrong", exception.getMessage());

    }

    @Test
    void shouldNotCreateBecauseUserIsAlreadyExist() throws ValidationException {
        UserController userController = new UserController();
        User user = new User("mail@mail.ru", "login", LocalDate.parse("2022-01-27"));
        User user1 = new User("mail@mail.ru", "login", LocalDate.parse("2022-01-27"));
        userController.create(user);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.create(user1));
        assertEquals("oh, something was wrong", exception.getMessage());

    }

    @Test
    void shouldNotCreateBecauseLoginIsInvalid() {
        UserController userController = new UserController();
        User user2 = new User("mail@mail.ru", "log in", LocalDate.parse("2022-01-27"));
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.create(user2));
        assertEquals("oh, something was wrong", exception.getMessage());
    }

    @Test
    void shouldNotCreateBecauseLoginIsEmpty() {
        UserController userController = new UserController();
        User user = new User("mail@mail.ru", "", LocalDate.parse("2022-01-27"));
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.create(user));
        assertEquals("oh, something was wrong", exception.getMessage());
    }

    @Test
    void shouldNotCreateBecauseBirthDayIsInFuture() {
        UserController userController = new UserController();
        User user = new User("mail@mail.ru", "login", LocalDate.parse("2023-01-27"));
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.create(user));
        assertEquals("oh, something was wrong", exception.getMessage());
    }

    @Test
    void shouldUpdate() throws ValidationException {
        UserController userController = new UserController();
        User user = new User("mail@mail.ru", "login", LocalDate.parse("2022-01-27"));
        User user2 = new User("mail@mail.ru", "login2", LocalDate.parse("2022-01-27"));
        user2.setId(1);
        userController.create(user);
        userController.update(user2);
        assertTrue(userController.findAll().contains(user2));
        assertFalse(userController.findAll().contains(user));
    }
}
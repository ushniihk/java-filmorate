package ru.yandex.practicum.filmorate.controller;


import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.CreatingException;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    @Test
    void findAll() {
    }

    @Test
    void shouldCreateFilm() throws ValidationException {
        FilmController filmController = new FilmController(new FilmService(new InMemoryFilmStorage(),
                new InMemoryUserStorage()));
        Film film1 = new Film("Matrix", "about matrix", LocalDate.parse("1895-12-28"), 100);
        filmController.create(film1);
        assertTrue(filmController.findAll().contains(film1));
    }

    @Test
    void shouldNotCreateBecauseReleaseInPast() {
        FilmController filmController = new FilmController(new FilmService(new InMemoryFilmStorage(),
                new InMemoryUserStorage()));
        Film film1 = new Film("Matrix", "about matrix", LocalDate.parse("1895-12-27"), 100);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film1));
        assertEquals("oh, something was wrong", exception.getMessage());
    }

    @Test
    void shouldNotCreateBecauseFilmIsAlreadyExist() throws ValidationException {
        FilmController filmController = new FilmController(new FilmService(new InMemoryFilmStorage(),
                new InMemoryUserStorage()));
        Film film1 = new Film("Matrix", "about matrix", LocalDate.parse("1895-12-28"), 100);
        Film film2 = new Film("Matrix", "about matrix", LocalDate.parse("1895-12-28"), 100);
        filmController.create(film1);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film2));
        assertEquals("oh, something was wrong", exception.getMessage());
    }


    @Test
    void shouldNotCreateBecauseFilmNameIsEmpty() {
        FilmController filmController = new FilmController(new FilmService(new InMemoryFilmStorage(),
                new InMemoryUserStorage()));
        Film film1 = new Film("", "about matrix", LocalDate.parse("2000-05-18"), 100);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film1));
        assertEquals("oh, something was wrong", exception.getMessage());
    }

    @Test
    void shouldNotCreateBecauseFilmDescriptionIsLongerThan200() {
        FilmController filmController = new FilmController(new FilmService(new InMemoryFilmStorage(),
                new InMemoryUserStorage()));
        Film film1 = new Film("Matrix"
                , "about matrixasdasdasdasddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd"
                , LocalDate.parse("2000-05-18"), 100);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film1));
        assertEquals("oh, something was wrong", exception.getMessage());
    }

    @Test
    void shouldNotCreateBecauseDurationIsNegative() {
        FilmController filmController = new FilmController(new FilmService(new InMemoryFilmStorage(),
                new InMemoryUserStorage()));
        Film film1 = new Film("Matrix", "about matrix", LocalDate.parse("1023-05-18"), -100);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film1));
        assertEquals("oh, something was wrong", exception.getMessage());
    }

    @Test
    void shouldUpdate() throws ValidationException, NotFoundParameterException {
        FilmController filmController = new FilmController(new FilmService(new InMemoryFilmStorage(),
                new InMemoryUserStorage()));
        Film film1 = new Film("Matrix", "about matrix", LocalDate.parse("1895-12-28"), 100);
        filmController.create(film1);
        Film film2 = new Film("Matrix2", "about matrix", LocalDate.parse("1895-12-28"), 100);
        film2.setId(1);
        filmController.update(film2);
        assertTrue(filmController.findAll().contains(film2));
        assertFalse(filmController.findAll().contains(film1));
    }

    @Test
    void shouldFindFilm() throws ValidationException, NotFoundParameterException {
        FilmController filmController = new FilmController(new FilmService(new InMemoryFilmStorage(),
                new InMemoryUserStorage()));
        Film film1 = new Film("Matrix", "about matrix", LocalDate.parse("1895-12-28"), 100);
        filmController.create(film1);
        Film film2 = new Film("Matrix2", "about matrix", LocalDate.parse("1895-12-28"), 100);
        filmController.create(film2);
        assertEquals(filmController.findFilm(2), film2);
        assertEquals(filmController.findFilm(1), film1);
    }

    @Test
    void shouldUserLikesTheFilm() throws ValidationException, NotFoundParameterException, CreatingException {
        FilmController filmController = new FilmController(new FilmService(new InMemoryFilmStorage(),
                new InMemoryUserStorage()));
        Film film1 = new Film("Matrix", "about matrix", LocalDate.parse("1895-12-28"), 100);
        filmController.create(film1);
        User user = new User("mail@mail.ru", "login", LocalDate.parse("2022-01-27"));
        filmController.getFilmService().getUserStorage().create(user);
        filmController.userLikesTheFilm(1, 1);
        assertEquals(film1.getLikes().size(), 1);
        assertTrue(film1.getLikes().contains(1));
        assertFalse(film1.getLikes().contains(2));
    }

    @Test
    void shouldDeleteLike() throws ValidationException, NotFoundParameterException, CreatingException {
        FilmController filmController = new FilmController(new FilmService(new InMemoryFilmStorage(),
                new InMemoryUserStorage()));
        Film film1 = new Film("Matrix", "about matrix", LocalDate.parse("1895-12-28"), 100);
        filmController.create(film1);
        User user = new User("mail@mail.ru", "login", LocalDate.parse("2022-01-27"));
        filmController.getFilmService().getUserStorage().create(user);
        filmController.userLikesTheFilm(1, 1);
        filmController.userDeleteLike(1, 1);
        assertEquals(film1.getLikes().size(), 0);
        assertFalse(film1.getLikes().contains(1));
    }

    @Test
    void shouldGetTopFilms() throws ValidationException, NotFoundParameterException, CreatingException, IncorrectParameterException {
        FilmController filmController = new FilmController(new FilmService(new InMemoryFilmStorage(),
                new InMemoryUserStorage()));
        Film film1 = new Film("Matrix", "about matrix", LocalDate.parse("1895-12-28"), 100);
        filmController.create(film1);
        Film film2 = new Film("Matrix2", "about matrix", LocalDate.parse("1895-12-28"), 100);
        filmController.create(film2);
        User user = new User("mail@mail.ru", "login", LocalDate.parse("2022-01-27"));
        filmController.getFilmService().getUserStorage().create(user);
        filmController.userLikesTheFilm(1, 1);
        assertEquals(filmController.getTopFilms(1).size(), 1);
        assertTrue(filmController.getTopFilms(1).contains(film1));
        assertEquals(filmController.getTopFilms(2).size(), 2);
        assertTrue(filmController.getTopFilms(2).contains(film2));
    }
}
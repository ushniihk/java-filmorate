package ru.yandex.practicum.filmorate.controller;


import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    @Test
    void findAll() {

    }

    @Test
    void shouldCreateFilm() throws ValidationException {
        FilmController filmController = new FilmController();
        Film film1 = new Film(  "Matrix", "about matrix", LocalDate.parse("1895-12-28"), 100);
        filmController.create(film1);
        assertTrue(filmController.findAll().contains(film1));
    }

    @Test
    void shouldNotCreateBecauseReleaseInPast () {
        FilmController filmController = new FilmController();
        Film film1 = new Film( "Matrix", "about matrix", LocalDate.parse("1895-12-27"), 100);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film1));
        assertEquals("oh, something was wrong", exception.getMessage());
    }

    @Test
    void shouldNotCreateBecauseFilmIsAlreadyExist () throws ValidationException {
        FilmController filmController = new FilmController();
        Film film1 = new Film( "Matrix", "about matrix", LocalDate.parse("1895-12-28"), 100);
        Film film2 = new Film( "Matrix", "about matrix", LocalDate.parse("1895-12-28"), 100);
        filmController.create(film1);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film2));
        assertEquals("oh, something was wrong", exception.getMessage());
    }


    @Test
    void shouldNotCreateBecauseFilmNameIsEmpty() {
        FilmController filmController = new FilmController();
        Film film1 = new Film( "", "about matrix", LocalDate.parse("2000-05-18"), 100);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film1));
        assertEquals("oh, something was wrong", exception.getMessage());
    }

    @Test
    void shouldNotCreateBecauseFilmDescriptionIsLongerThan200() {
        FilmController filmController = new FilmController();
        Film film1 = new Film( "Matrix"
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
    void shouldNotCreateBecauseDurationIsNegative () {
        FilmController filmController = new FilmController();
        Film film1 = new Film( "Matrix", "about matrix", LocalDate.parse("1023-05-18"), -100);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.create(film1));
        assertEquals("oh, something was wrong", exception.getMessage());
    }

    @Test
    void shouldUpdate() throws ValidationException {
        FilmController filmController = new FilmController();
        Film film1 = new Film(  "Matrix", "about matrix", LocalDate.parse("1895-12-28"), 100);
        filmController.create(film1);
        Film film2 = new Film(  "Matrix2", "about matrix", LocalDate.parse("1895-12-28"), 100);
        film2.setId(1);
        filmController.update(film2);
        assertTrue(filmController.findAll().contains(film2));
        assertFalse(filmController.findAll().contains(film1));
    }
}
package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> findAll();

    Film create(Film film) throws ValidationException;

    Film update(Film film) throws NotFoundParameterException;

    boolean deleteFilm(Integer filmId);

    Optional<Film> getFilm(Integer id) throws NotFoundParameterException;

    void createLike(Integer filmID, Integer userID);

    void removeLike(Integer filmID, Integer userID);

    Film makeFilm(ResultSet rs) throws SQLException;

    Collection<Film> findFilmsByDirector(Integer directorID, String sortBy) throws NotFoundParameterException;

    Collection<Film> getCommonFilms(Integer userId, Integer friendId);
}

package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> findAll();

    Film create(Film film) throws ValidationException;

    Film update(Film film) throws NotFoundParameterException;

    Optional<Film> getFilm(Integer id) throws NotFoundParameterException;

    void createLike(Integer filmID, Integer userID);

    void removeLike(Integer filmID, Integer userID);
}

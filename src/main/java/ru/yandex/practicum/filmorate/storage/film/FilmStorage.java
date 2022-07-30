package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> findAll();

    Film create(Film film) throws ValidationException;

    Film update(Film film) throws NotFoundParameterException;

    boolean delete(Integer filmId);

    Optional<Film> get(Integer id) throws NotFoundParameterException;

    void createLike(Integer filmID, Integer userID, Integer mark);

    void removeLike(Integer filmID, Integer userID);

    Map<Integer, Integer> getLikes(Integer id);

    Film make(ResultSet rs) throws SQLException;

    Collection<Film> findByDirector(Director director, String sortBy) throws NotFoundParameterException;

    Collection<Film> getCommon(Integer userId, Integer friendId);

    Collection<Film> searchAnyway(String query, String type);

    Collection<Film> getPopularByGenreAndYear(Integer genreId, String year);
}

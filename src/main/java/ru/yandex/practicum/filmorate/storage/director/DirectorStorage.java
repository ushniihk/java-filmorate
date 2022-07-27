package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public interface DirectorStorage {

    Collection<Director> findAll();

    Director get(Integer id) throws NotFoundParameterException;

    void addToFilm(Director director, Film film);

    void remove(Integer directorID);

    Collection<Director> getByFilm(Integer id);

    Director make(ResultSet rs) throws SQLException;

    Director update(Director director);

    Director create(Director director);

    void delete(Integer directorID);

}

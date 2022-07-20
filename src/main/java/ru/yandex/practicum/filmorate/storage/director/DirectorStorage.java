package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.model.Director;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public interface DirectorStorage {

    Collection<Director> findAll();

    Director getDirector(Integer id) throws NotFoundParameterException;

    void addDirector(Integer ID, Integer filmID);

    void removeDirector(Integer directorID);

    Collection<Director> getDirectors(Integer id);

    Director makeDirectors(ResultSet rs) throws SQLException;

    Director updateDirector(Director director);

    Director createDirector(Director director);

    void deleteDirector(Integer directorID);

}

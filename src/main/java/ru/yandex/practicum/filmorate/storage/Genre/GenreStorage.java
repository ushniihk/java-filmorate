package ru.yandex.practicum.filmorate.storage.Genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenreStorage {
    Collection<Genre> findAll();

    Genre get(Integer id);

    void create(Integer ID, Integer film_id);

    void remove(Integer genreID);

    Collection<Genre> findByFilm(Integer filmID);

}

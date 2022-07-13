package ru.yandex.practicum.filmorate.storage.Genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenreStorage {
    Collection<Genre> findAll();

    Genre getGenre(Integer id);

    void createGenre(Integer ID, Integer film_id);

    void removeGenre(Integer genreID);

}

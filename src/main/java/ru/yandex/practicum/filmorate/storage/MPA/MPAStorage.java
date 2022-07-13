package ru.yandex.practicum.filmorate.storage.MPA;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Collection;

public interface MPAStorage {
    Collection<MPA> findAll();

    MPA getMPA(Integer id);

    void createMPA(MPA mpa, Integer filmId);

}

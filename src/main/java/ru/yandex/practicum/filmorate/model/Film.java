package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import ru.yandex.practicum.filmorate.model.enums.Genre;
import ru.yandex.practicum.filmorate.model.enums.Rating_MPA;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Data
public class Film {
    @Setter
    @EqualsAndHashCode.Exclude
    private int id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
    private final Genre genre;
    private final Rating_MPA rating_mpa;

    @EqualsAndHashCode.Exclude
    private Set<Integer> likes = new TreeSet<>();

    public void addLike(Integer userId) {
        likes.add(userId);
    }

    public void deleteLike(Integer userId) {
        likes.remove(userId);
    }

}

package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
public class Film {
    @EqualsAndHashCode.Exclude
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    @EqualsAndHashCode.Exclude
    private Collection<Genre> genres = new ArrayList<>();
    @EqualsAndHashCode.Exclude
    private MPA mpa;
    @EqualsAndHashCode.Exclude
    private Integer rate;
    @EqualsAndHashCode.Exclude
    private Map<Integer, Integer> likes = new HashMap<>();
    @EqualsAndHashCode.Exclude
    private Collection<Director> directors = new ArrayList<>();

    public Film() {
        super();
    }

    public Film(Integer id, String name, String description, Date releaseDate, Integer duration,
                Collection<Genre> genres, MPA mpa, Integer rate, Map<Integer, Integer> likes, Collection<Director> directors) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate.toLocalDate();
        this.duration = duration;
        this.genres = genres;
        this.mpa = mpa;
        this.rate = rate;
        this.likes = likes;
        this.directors = directors;
    }

    public void addLike(Integer userId, Integer mark) {
        likes.put(userId, mark);
    }

    public void deleteLike(Integer userId) {
        likes.remove(userId);
    }

    public void setNullDirectors(Collection<Director> directors) {
        if (directors.size() == 0)
            this.directors = null;
    }
}

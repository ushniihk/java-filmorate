package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.util.Comparator;

@Data
public class Genre implements Comparator<Genre> {
    private Integer id;
    private String name;

    public Genre(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Genre() {
        super();
    }

    @Override
    public int compare(Genre o1, Genre o2) {
        return o1.getId() - o2.getId();
    }
}

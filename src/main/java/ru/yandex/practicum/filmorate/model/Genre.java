package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Comparator;

@Data
@AllArgsConstructor
public class Genre implements Comparator<Genre> {

    private Integer id;
    private String name;

    @Override
    public int compare(Genre o1, Genre o2) {
        return o1.getId() - o2.getId();
    }
}

package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class Director {
    private Integer id;
    @EqualsAndHashCode.Exclude
    private String name;

    public Director(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Director() {
        super();
    }
}

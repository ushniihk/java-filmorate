package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
public class Director {

    private Integer id;
    @EqualsAndHashCode.Exclude
    private String name;

}

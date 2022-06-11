package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.time.LocalDate;
@Data
public class User {
    @Setter
    @EqualsAndHashCode.Exclude
    private int id;
    private final String email;
    private final String login;
    @Setter
    @EqualsAndHashCode.Exclude
    private String name;
    private final LocalDate birthday;
}

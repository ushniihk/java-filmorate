package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;


@Data
public class User {

    @EqualsAndHashCode.Exclude
    private int id;
    private String email;
    private String login;
    @Setter
    @EqualsAndHashCode.Exclude
    private String name;
    private LocalDate birthday;
    @EqualsAndHashCode.Exclude
    private Collection<Integer> friends = new ArrayList<>();

    public User() {
        super();
    }

    public User(int id, String email, String login, Collection<Integer> friends, String name, Date birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.friends = friends;
        this.name = name;
        this.birthday = birthday.toLocalDate();
    }

}

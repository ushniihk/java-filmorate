package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

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
    @EqualsAndHashCode.Exclude
    private Set<Integer> friends = new TreeSet<>();

    public void addFriend(Integer userId) {
        friends.add(userId);
    }

    public void removeFriend(Integer userId) {
        friends.remove(userId);
    }
}

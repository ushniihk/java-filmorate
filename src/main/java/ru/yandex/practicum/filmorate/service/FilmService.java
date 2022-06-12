package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class FilmService {

    public Integer addLike(Film film, User user) {
        film.addLike(user.getId());
        return film.getLikes().size();
    }

    public Integer deleteLike(Film film, User user) {
        film.deleteLike(user.getId());
        return film.getLikes().size();
    }

    public Collection<Film> getTopFilmsByLikes(Collection<Film> films, Integer count) {
        return films.stream()
                .sorted(Comparator.comparingInt(f -> f.getLikes().size() * (-1)))
                .limit(count)
                .collect(Collectors.toList());
    }

}

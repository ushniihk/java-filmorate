package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@Data
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public void addLike(Integer id, Integer userId) throws NotFoundParameterException {
        filmStorage.getFilm(id).addLike(userStorage.getUser(userId).getId());
    }

    public void deleteLike(Integer id, Integer userId) throws NotFoundParameterException {
        filmStorage.getFilm(id).deleteLike(userStorage.getUser(userId).getId());
    }

    public Collection<Film> getTopFilmsByLikes(Collection<Film> films, Integer count) {
        return films.stream()
                .sorted(Comparator.comparingInt(f -> f.getLikes().size() * (-1)))
                .limit(count)
                .collect(Collectors.toList());
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) throws ValidationException {
        return filmStorage.create(film);
    }

    public Film update(Film film) throws NotFoundParameterException {
        return filmStorage.update(film);
    }

    public Film getFilm(Integer id) throws NotFoundParameterException {
        return filmStorage.getFilm(id);
    }

}

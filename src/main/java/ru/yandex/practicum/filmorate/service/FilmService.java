package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public void addLike(Integer id, Integer userId) throws NotFoundParameterException {
        if (checkID(id))
            throw new NotFoundParameterException("bad id");
        if (checkID(userId))
            throw new NotFoundParameterException("bad id");

        Optional<Film> film = filmStorage.getFilm(id);
        Optional<User> user = userStorage.getUser(userId);

        if (film.isPresent() && user.isPresent()) {
            film.get().addLike(user.get().getId());
            filmStorage.createLike(id, userId);
        }
    }

    public void deleteLike(Integer id, Integer userId) throws NotFoundParameterException {
        if (checkID(id))
            throw new NotFoundParameterException("bad id");
        if (checkID(userId))
            throw new NotFoundParameterException("bad id");

        Optional<Film> film = filmStorage.getFilm(id);
        Optional<User> user = userStorage.getUser(userId);

        if (film.isPresent() && user.isPresent()) {
            film.get().deleteLike(user.get().getId());
            filmStorage.removeLike(id, userId);
        }
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

    public Optional<Film> getFilm(Integer id) throws NotFoundParameterException {
        if (checkID(id))
            throw new NotFoundParameterException("bad id");
        return filmStorage.getFilm(id);
    }

    public void removeFilm(int filmId) throws NotFoundParameterException {
        if (checkID(filmId)) {
            throw new NotFoundParameterException("bad id");
        }
        boolean deleted = filmStorage.removeFilm(filmId);
        if (!deleted) {
            throw new NotFoundParameterException("No Film With Such Id");
        }
    }

    public Collection<Film> findFilmsByDirector(Integer directorID, String sortBy) throws NotFoundParameterException {
        if (checkID(directorID))
            throw new NotFoundParameterException("bad id");
        return filmStorage.findFilmsByDirector(directorID, sortBy);
    }

    private boolean checkID(Integer id) {
        return (id == null || id < 0);
    }
}


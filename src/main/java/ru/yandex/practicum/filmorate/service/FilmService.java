package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;
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
    private final DirectorStorage directorStorage;

    public void addLike(Integer id, Integer userId, Integer mark) throws NotFoundParameterException {
        if (checkID(id) || checkID(userId))
            throw new NotFoundParameterException("bad id");

        Optional<Film> film = filmStorage.get(id);
        Optional<User> user = userStorage.get(userId);

        if (film.isPresent() && user.isPresent()) {
            film.get().addLike(user.get().getId(), mark);
            filmStorage.createLike(id, userId, mark);
        }
    }

    public void deleteLike(Integer id, Integer userId) throws NotFoundParameterException {
        if (checkID(id))
            throw new NotFoundParameterException("bad id");
        if (checkID(userId))
            throw new NotFoundParameterException("bad id");

        Optional<Film> film = filmStorage.get(id);
        Optional<User> user = userStorage.get(userId);

        if (film.isPresent() && user.isPresent()) {
            film.get().deleteLike(user.get().getId());
            filmStorage.removeLike(id, userId);
        }
    }

    private Collection<Film> getTopFilmsByLikes(Collection<Film> films, Integer count) {
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

    public Film get(Integer id) throws NotFoundParameterException {
        if (checkID(id))
            throw new NotFoundParameterException("bad id");
        return filmStorage.get(id).orElseThrow(() -> new NotFoundParameterException("No Film With Such Id"));
    }

    public void delete(int filmId) throws NotFoundParameterException {
        if (checkID(filmId)) {
            throw new NotFoundParameterException("bad id");
        }
        boolean deleted = filmStorage.delete(filmId);
        if (!deleted) {
            throw new NotFoundParameterException("No Film With Such Id");
        }
    }

    public Collection<Film> findByDirector(Integer directorID, String sortBy) throws NotFoundParameterException {
        if (checkID(directorID))
            throw new NotFoundParameterException("bad id");
        return filmStorage.findByDirector(directorStorage.get(directorID), sortBy);
    }

    private boolean checkID(Integer id) {
        return (id == null || id < 0);
    }

    public Collection<Film> getCommon(Integer userId, Integer friendId) throws NotFoundParameterException {
        if (checkID(userId) || checkID(friendId))
            throw new NotFoundParameterException("bad id");
        Collection<Film> commonFilms = filmStorage.getCommon(userId, friendId);
        return getTopFilmsByLikes(commonFilms, commonFilms.size());
    }

    public Collection<Film> search(String query, String type) {
        return filmStorage.searchAnyway(query, type);
    }

    public Collection<Film> getPopularByParams(Integer limit, Integer genreId, String year) throws IncorrectParameterException {
        Collection<Film> theMostPopularFilms;
        if (limit <= 0)
            throw new IncorrectParameterException("count");
        if (genreId != null || year != null) {
            theMostPopularFilms = filmStorage.getPopularByGenreAndYear(genreId, year);
        } else {
            theMostPopularFilms = findAll();
        }
        return getTopFilmsByLikes(theMostPopularFilms, limit);
    }
}
package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final ConcurrentMap<Integer, Film> films = new ConcurrentHashMap();
    private int counterID = 1;

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film create(Film film) throws ValidationException {
        if (films.containsValue(film) || (!StringUtils.hasText(film.getName())) || (film.getDescription().length() > 200)
                || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))
                || (film.getDuration() < 0)) {
            log.debug("Oh, no. validation failed");
            throw new ValidationException("oh, something was wrong");
        } else
            film.setId(counterID++);
        films.put(film.getId(), film);
        log.debug("added: {}", film.toString());
        return film;
    }

    @Override
    public Film update(Film film) throws NotFoundParameterException {
        if ((!StringUtils.hasText(film.getName())) || (film.getDescription().length() > 200)
                || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))
                || (film.getDuration() < 0) || (film.getId() < 0)) {
            log.debug("Oh, no. validation failed");
            throw new NotFoundParameterException("oh, something was wrong");
        } else
            films.put(film.getId(), film);
        log.debug("updated: {}", film.toString());
        return film;
    }

    @Override
    public Film getFilm(Integer id) throws NotFoundParameterException {
        if (checkID(id))
            throw new NotFoundParameterException("bad id");
        return films.get(id);
    }

    private boolean checkID(Integer id) {
        return   (id == null || id <= 0);
    }

}
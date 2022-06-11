package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private ConcurrentMap<Integer, Film> films = new ConcurrentHashMap();
    private int counterID = 1;

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
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

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException {
            if ((!StringUtils.hasText(film.getName())) || (film.getDescription().length() > 200)
                    || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))
                    || (film.getDuration() < 0) || (film.getId() < 0)) {
                log.debug("Oh, no. validation failed");
                throw new ValidationException("oh, something was wrong");
            } else
                films.put(film.getId(), film);
            log.debug("updated: {}", film.toString());
        return film;
    }
}

package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@Slf4j
@Data
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws NotFoundParameterException {
        return filmService.update(film);
    }

    @GetMapping("/{id}")
    public Film findFilm(@PathVariable Integer id) throws NotFoundParameterException {
        return filmService.getFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void userLikesTheFilm(@PathVariable Integer id, @PathVariable Integer userId) throws NotFoundParameterException {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void userDeleteLike(@PathVariable Integer id, @PathVariable Integer userId) throws NotFoundParameterException {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getTopFilms(@RequestParam(defaultValue = "10") Integer count) throws IncorrectParameterException {
        if (count <= 0)
            throw new IncorrectParameterException("count");
        return filmService.getTopFilmsByLikes(filmService.findAll(), count);
    }
}

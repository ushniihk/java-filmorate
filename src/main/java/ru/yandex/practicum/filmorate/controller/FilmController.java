package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    FilmStorage filmStorage;
    FilmService filmService;
    UserStorage userStorage;

    public FilmController(FilmStorage filmStorage, UserStorage userStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
        this.userStorage = userStorage;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        return filmStorage.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws NotFoundParameterException {
       return filmStorage.update(film);
    }

    @GetMapping("/{id}")
    public Film findFilm(@PathVariable Integer id) throws NotFoundParameterException {
        return filmStorage.getFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public Integer userLikesTheFilm(@PathVariable Integer id, @PathVariable Integer userId) throws NotFoundParameterException {
        return filmService.addLike(filmStorage.getFilm(id), userStorage.getUser(userId));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Integer userDeleteLike(@PathVariable Integer id, @PathVariable Integer userId) throws NotFoundParameterException {
        return filmService.deleteLike(filmStorage.getFilm(id), userStorage.getUser(userId));
    }

    @GetMapping("/popular")
    public Collection<Film> getTopFilms(@RequestParam(defaultValue = "10") Integer count) throws IncorrectParameterException {
        if (count <= 0)
            throw new IncorrectParameterException("count");
        return filmService.getTopFilmsByLikes(filmStorage.findAll(), count);
    }

}

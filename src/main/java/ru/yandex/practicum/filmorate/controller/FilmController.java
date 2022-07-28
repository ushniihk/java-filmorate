package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.websocket.server.PathParam;
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
    public Film get(@PathVariable Integer id) throws NotFoundParameterException {
        return filmService.get(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void userLikesTheFilm(@PathVariable Integer id, @PathVariable Integer userId,
                                 @RequestParam(defaultValue = "0") Integer mark) throws NotFoundParameterException {
        filmService.addLike(id, userId, mark);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void userDeleteLike(@PathVariable Integer id, @PathVariable Integer userId) throws NotFoundParameterException {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getTop(@RequestParam(defaultValue = "10") Integer count,
                                   @RequestParam(required = false) Integer genreId,
                                   @RequestParam(required = false) String year) throws IncorrectParameterException {
        return filmService.getPopularByParams(count, genreId, year);
    }

    @GetMapping("/director/{directorId}")
    public Collection<Film> getDirectors(@PathVariable Integer directorId, @RequestParam String sortBy) throws NotFoundParameterException {
        return filmService.findByDirector(directorId, sortBy);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int filmId) throws NotFoundParameterException {
        filmService.delete(filmId);
    }

    @GetMapping("/common")
    public Collection<Film> getCommon(@RequestParam Integer userId, @RequestParam Integer friendId) throws NotFoundParameterException {
        return filmService.getCommon(userId, friendId);
    }

    @GetMapping("/search")
    public Collection<Film> search(@PathParam(value = "query") String query, @PathParam(value = "by") String by) {
        return filmService.search(query, by);
    }
}
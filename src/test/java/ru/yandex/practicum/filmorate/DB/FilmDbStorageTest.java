package ru.yandex.practicum.filmorate.DB;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final FilmDbStorage filmStorage;

    @Test
    public void shouldFindFilmById() {
        assertEquals(filmStorage.get(2).get().getName(), "matrix2");
        assertEquals(filmStorage.get(3).get().getId(), 3);
        assertEquals(filmStorage.get(1).get().getDuration(), 100);
    }

    @Test
    public void shouldFindAllFilms() {
        assertTrue(filmStorage.findAll().contains(filmStorage.get(2).get()));
        assertTrue(filmStorage.findAll().contains(filmStorage.get(1).get()));
        assertTrue(filmStorage.findAll().contains(filmStorage.get(3).get()));
    }

    @Test
    public void shouldCreateAndUpdateFilm() throws ValidationException, NotFoundParameterException {
        Film film1 = new Film(1, "JavaFilm", "about Java", Date.valueOf("1895-12-28"),
                100, new ArrayList<>(), new MPA(1, "one"), 6, new HashMap<>(), new ArrayList<>());
        filmStorage.create(film1);
        assertEquals(filmStorage.findAll().size(), 4);
        Film film2 = new Film(1, "Test Film", "about Test", Date.valueOf("1895-12-28"),
                100, new ArrayList<>(), new MPA(1, "one"), 6, new HashMap<>(), new ArrayList<>());
        filmStorage.update(film2);
        assertEquals(filmStorage.findAll().size(), 4);
    }

    @Test
    public void shouldAddLikeAndDelete() {
        assertEquals(filmStorage.get(3).get().getLikes().size(), 0);
        filmStorage.createLike(3, 1, 6);
        assertEquals(filmStorage.get(3).get().getLikes().size(), 1);
        filmStorage.removeLike(3, 1);
        assertEquals(filmStorage.get(3).get().getLikes().size(), 0);

    }

}

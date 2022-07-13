package ru.yandex.practicum.filmorate.DB;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.Genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTest {

    private final GenreDbStorage genreDbStorage;
    private final FilmDbStorage filmDbStorage;

    @Test
    public void shouldFindGenreById() {
        assertEquals(genreDbStorage.getGenre(1).getName(), "Комедия");
        assertEquals(genreDbStorage.getGenre(2).getName(), "Драма");
        assertEquals(genreDbStorage.getGenre(3).getName(), "Мультфильм");
    }

    @Test
    public void shouldFindAllGenres() {
        assertTrue(genreDbStorage.findAll().contains(genreDbStorage.getGenre(1)));
        assertTrue(genreDbStorage.findAll().contains(genreDbStorage.getGenre(2)));
        assertTrue(genreDbStorage.findAll().contains(genreDbStorage.getGenre(3)));
    }

    @Test
    public void shouldCreateAndDeleteGenre() {
        genreDbStorage.createGenre(1, 1);
        assertTrue(filmDbStorage.getFilm(1).get().getGenres().contains(genreDbStorage.getGenre(1)));
        genreDbStorage.removeGenre(1);
        assertFalse(filmDbStorage.getFilm(1).get().getGenres().contains(genreDbStorage.getGenre(1)));
    }


}

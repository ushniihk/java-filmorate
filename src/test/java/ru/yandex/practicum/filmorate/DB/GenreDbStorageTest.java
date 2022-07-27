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
        assertEquals(genreDbStorage.get(1).getName(), "Комедия");
        assertEquals(genreDbStorage.get(2).getName(), "Драма");
        assertEquals(genreDbStorage.get(3).getName(), "Мультфильм");
    }

    @Test
    public void shouldFindAllGenres() {
        assertTrue(genreDbStorage.findAll().contains(genreDbStorage.get(1)));
        assertTrue(genreDbStorage.findAll().contains(genreDbStorage.get(2)));
        assertTrue(genreDbStorage.findAll().contains(genreDbStorage.get(3)));
    }

    @Test
    public void shouldCreateAndDeleteGenre() {
        genreDbStorage.create(1, 1);
        assertTrue(filmDbStorage.get(1).get().getGenres().contains(genreDbStorage.get(1)));
        genreDbStorage.remove(1);
        assertFalse(filmDbStorage.get(1).get().getGenres().contains(genreDbStorage.get(1)));
    }


}

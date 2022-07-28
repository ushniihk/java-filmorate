package ru.yandex.practicum.filmorate.DB;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.MPA.MPADbStorage;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDbStorageTest {

    private final MPADbStorage mpaDbStorage;

    @Test
    public void shouldFindAllMPA() {
        assertEquals(Set.copyOf(mpaDbStorage.findAll()).size(), 5);
    }

    @Test
    public void shouldFindMPAById() {
        assertEquals(mpaDbStorage.get(1).getName(), "G");
    }
}

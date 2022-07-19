package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.CreatingException;
import ru.yandex.practicum.filmorate.exceptions.UpdateException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import java.util.Collection;

@Service
@Data
@RequiredArgsConstructor
public class DirectorService {

    private final DirectorStorage directorStorage;

    public Collection<Director> findAll() {
        return directorStorage.findAll();
    }

    public Director create(Director director) throws CreatingException {
        return directorStorage.createDirector(director);
    }

    public Director update(@RequestBody Director director) throws UpdateException {
        return directorStorage.updateDirector(director);
    }

    public Director getDirector(@PathVariable Integer id) {
        return directorStorage.getDirector(id);
    }

    public void deleteDirector(@PathVariable Integer id) {
        directorStorage.deleteDirector(id);
    }



}

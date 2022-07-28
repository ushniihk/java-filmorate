package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exceptions.CreatingException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
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
        if (director.getName().isBlank() || director.getName().isEmpty())
            throw new CreatingException("Bad name");
        return directorStorage.create(director);
    }

    public Director update(@RequestBody Director director) throws UpdateException {
        if (!directorStorage.findAll().contains(director))
            throw new UpdateException("Bad director");
        return directorStorage.update(director);
    }

    public Director get(@PathVariable Integer id) throws NotFoundParameterException {
        return directorStorage.get(id);
    }

    public void delete(@PathVariable Integer id) {
        directorStorage.delete(id);
    }


}

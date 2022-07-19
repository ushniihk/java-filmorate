package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.CreatingException;
import ru.yandex.practicum.filmorate.exceptions.UpdateException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.Collection;

@RestController
@Slf4j
@Data
@RequestMapping("/directors")
public class DirectorController {

    private final DirectorService directorService;

    @GetMapping
    public Collection<Director> findAll() {
        return directorService.findAll();
    }

    @PostMapping
    public Director create(@RequestBody Director director) throws CreatingException {
        return directorService.create(director);
    }

    @PutMapping
    public Director update(@RequestBody Director director) throws UpdateException {
        return directorService.update(director);
    }

    @GetMapping("/{id}")
    public Director getDirector(@PathVariable Integer id) {
        return directorService.getDirector(id);
    }

    @DeleteMapping("/{id}")
    public void deleteDirector(@PathVariable Integer id) {
        directorService.deleteDirector(id);
    }
}

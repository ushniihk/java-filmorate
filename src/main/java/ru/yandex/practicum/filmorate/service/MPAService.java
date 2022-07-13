package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPA.MPAStorage;

import java.util.Collection;

@Service
public class MPAService {

    private final MPAStorage mpaStorage;

    public MPAService(MPAStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public Collection<MPA> findAll() {
        return mpaStorage.findAll();
    }

    public MPA getMPA(Integer id) throws NotFoundParameterException {
        if (checkID(id))
            throw new NotFoundParameterException("bad id");
        return mpaStorage.getMPA(id);
    }

    private boolean checkID(Integer id) {
        return (id == null || id < 0);
    }

}

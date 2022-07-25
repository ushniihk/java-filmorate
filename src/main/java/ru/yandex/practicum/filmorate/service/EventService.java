package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.Event.EventDao;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Service
@Data
@RequiredArgsConstructor
public class EventService {

    private final EventDao eventDao;
    private final UserStorage userStorage;

    public Collection<Event> getEvents(Integer userId) throws NotFoundParameterException {
        userStorage.getUser(userId);
        return eventDao.getEvents(userId);
    }
}

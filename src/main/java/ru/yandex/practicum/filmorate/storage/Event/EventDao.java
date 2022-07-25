package ru.yandex.practicum.filmorate.storage.Event;

import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperations;
import ru.yandex.practicum.filmorate.model.EventType;

import java.util.Collection;

public interface EventDao {

    Collection<Event> getEvents(Integer id);        //слежение за пользователем

    void add(Integer userId, Integer entityId, EventType eventType, EventOperations operation);     //добавить лог

}

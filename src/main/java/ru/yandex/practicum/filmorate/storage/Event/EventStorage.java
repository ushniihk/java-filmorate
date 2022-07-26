package ru.yandex.practicum.filmorate.storage.Event;

import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperations;
import ru.yandex.practicum.filmorate.model.EventType;

import java.util.Collection;

public interface EventStorage {

    Collection<Event> getEvents(Integer id);

    void add(Integer userId, Integer entityId, EventType eventType, EventOperations operation);

}

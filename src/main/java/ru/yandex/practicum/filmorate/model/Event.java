package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Event {

    private Long timestamp;
    private Integer userId;
    private String eventType;
    private String operation;
    private Integer eventId;
    private Long entityId;

    public Event(Long timestamp, Integer userId, Integer eventId, Long entityId, String eventType, String operation) {
        this.timestamp = timestamp;
        this.userId = userId;
        this.eventType = eventType;
        this.operation = operation;
        this.eventId = eventId;
        this.entityId = entityId;
    }
}

//        "timestamp": 123344556,
//        "userId": 123,
//        "eventType": "LIKE", // одно из значениий LIKE, REVIEW или FRIEND
//        "operation": "REMOVE", // одно из значениий REMOVE, ADD, UPDATE
//        "eventId": 1234, //primary key
//        "entityId": 1234   // идентификатор сущности, с которой произошло событие
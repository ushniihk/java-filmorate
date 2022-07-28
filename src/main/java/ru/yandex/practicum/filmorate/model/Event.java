package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Event {

    private Long timestamp;
    private Integer userId;
    private Integer eventId;
    private Long entityId;
    private String eventType;
    private String operation;
}
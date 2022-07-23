package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Review {

    private Integer id;
    private String content;
    private boolean isPositive;
    private Integer likesRatingUseful;
    private Integer userId;
    private Integer filmId;


}

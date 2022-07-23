package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class Review {

    private Integer id;
    private String content;
    private Boolean isPositive; // Тип отзыва — негативный/положительный.
    private Integer userId;
    private Integer filmId;

    private Collection<Integer> reviewLikes = new ArrayList<>(); // коллекция лайков
    private Collection<Integer> reviewDislikes = new ArrayList<>(); //коллекция дизлайков

    public void addReviewLike(Integer userId) {
        reviewLikes.add(userId);
    }

    public void deleteReviewLike(Integer userId) {
        reviewLikes.remove(userId);
    }

    public void addReviewDislike(Integer userId) {
        reviewDislikes.add(userId);
    }

    public void deleteReviewDislike(Integer userId) {
        reviewDislikes.remove(userId);
    }
}

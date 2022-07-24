package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class Review {
    @EqualsAndHashCode.Exclude
    private Integer reviewId;
    private String content;
    private Boolean isPositive; // тип отзыва — негативный/положительный.
    private Integer userId;
    private Integer filmId;
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Collection<Integer> reviewLikes; // коллекция лайков
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Collection<Integer> reviewDislikes; //коллекция дизлайков

    public Review(Integer reviewId, String content, Boolean isPositive, Integer userId, Integer filmId
    ) {
        this.reviewId = reviewId;
        this.content = content;
        this.isPositive = isPositive;
        this.userId = userId;
        this.filmId = filmId;
        this.reviewLikes=new ArrayList<>();
        this.reviewDislikes=new ArrayList<>();

    }

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

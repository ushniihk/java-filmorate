package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.exceptions.CreatingException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;
import java.util.Optional;

public interface ReviewStorage {

    Collection<Review> findAll();

    Review createReview(Review review) throws CreatingException;

    Review updateReview(Review review) throws NotFoundParameterException;

    boolean deleteReview(Integer reviewId);

    Optional<Review> getReview(Integer id) throws NotFoundParameterException;

    void createReviewLike(Integer reviewId, Integer userID);

    void deleteReviewLike(Integer reviewId, Integer userID);
    void createReviewDislike(Integer reviewId, Integer userID);

    void deleteReviewDislike(Integer reviewId, Integer userID);
}

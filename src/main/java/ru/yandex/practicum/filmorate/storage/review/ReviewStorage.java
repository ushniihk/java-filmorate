package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.exceptions.CreatingException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;
import java.util.Optional;

public interface ReviewStorage {

    Collection<Review> findAll();

    Review createReview(Review review) throws CreatingException, ValidationException, NotFoundParameterException;

    Review updateReview(Review review) throws NotFoundParameterException;

    void deleteReview(Integer reviewId);

    Optional<Review> getReview(Integer id) throws NotFoundParameterException;

    void createReviewLikeDislike(Integer reviewId, Integer userID, Integer value);

    void deleteReviewLikeDislike(Integer reviewId, Integer userID);

}

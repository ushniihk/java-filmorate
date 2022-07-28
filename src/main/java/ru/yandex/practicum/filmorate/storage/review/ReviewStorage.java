package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.exceptions.CreatingException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;
import java.util.Optional;

public interface ReviewStorage {

    Collection<Review> findAll();

    Review create(Review review) throws CreatingException, ValidationException, NotFoundParameterException;

    Review update(Review review) throws NotFoundParameterException;

    void delete(Integer reviewId);

    Optional<Review> get(Integer id) throws NotFoundParameterException;

    void createLikeDislike(Integer reviewId, Integer userID, Integer value);

    void deleteLikeDislike(Integer reviewId, Integer userID);

}

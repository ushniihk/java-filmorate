package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.exceptions.CreatingException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;
import java.util.Optional;

public interface ReviewStorage {

    Collection<Review> findAll();

    Review createReview(Review review) throws CreatingException;

    Review update(Review review) throws NotFoundParameterException;

    boolean deleteReview(Integer reviewId);

    Optional<Review> getReview(Integer id) throws NotFoundParameterException;

    void createLike(Integer filmID, Integer userID);

    void removeLike(Integer filmID, Integer userID);
}

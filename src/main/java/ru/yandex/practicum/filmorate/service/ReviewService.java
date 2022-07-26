package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.exceptions.CreatingException;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewStorage reviewStorage;
    private final UserStorage userStorage;

    private enum LikeValue {
        LIKE(1), DISLIKE(-1);
        private final Integer dbView;

        LikeValue(Integer dbView) {
            this.dbView = dbView;
        }

        public Integer getDbView() {
            return dbView;
        }
    }

    public Review createReview(Review review) throws CreatingException, NotFoundParameterException, ValidationException {
        if (checkRawReview(review)) {
            throw new NotFoundParameterException("Bad id or content");
        } else if (review.getIsPositive() == null) {
            throw new CreatingException("Bad type or no type");
        }
        return reviewStorage.createReview(review);
    }

    public Review updateReview(Review review) throws NotFoundParameterException, ValidationException, CreatingException {
        if (checkRawReview(review)) {
            throw new NotFoundParameterException("Bad id or content");
        } else if (!reviewStorage.findAll().contains(review)) {
            return reviewStorage.updateReview(review);
        } else {
            return reviewStorage.createReview(review);
        }
    }

    public void deleteReview(@PathVariable Integer id) {
        reviewStorage.deleteReview(id);
    }

    public Review getReview(@PathVariable Integer id) throws NotFoundParameterException {
        return reviewStorage.getReview(id).orElseThrow(() -> new NotFoundParameterException("No review with such id"));
    }

    public Collection<Review> findAll() {
        return reviewStorage.findAll().stream()
                .sorted(Comparator.comparingInt(r -> r.getUseful() * (-1)))
                .collect(Collectors.toList());
    }

    public Collection<Review> getTopReviewsByUseful(Collection<Review> reviews, Integer filmId, Integer count) throws IncorrectParameterException {
        if (count <= 0) throw new IncorrectParameterException("Bad count");
        return reviews.stream()
                .sorted(Comparator.comparingInt(r -> r.getFilmId() * (-1)))
                .filter(r -> Objects.equals(r.getFilmId(), filmId))
                .limit(count)
                .collect(Collectors.toList());
    }

    private boolean checkID(Integer id) {
        return (id == null || id < 0);
    }

    private boolean checkRawReview(Review review) {
        return review.getContent().isBlank() || review.getFilmId() < 0 || review.getUserId() < 0;
    }

    public void createReviewLike(Integer reviewId, Integer userId) throws NotFoundParameterException {
        if (checkID(reviewId) && checkID(userId)) throw new NotFoundParameterException("bad id");

        Optional<Review> review = reviewStorage.getReview(reviewId);
        Optional<User> user = userStorage.getUser(userId);

        if (review.isPresent() && user.isPresent()) {
            reviewStorage.createReviewLikeDislike(reviewId, userId, LikeValue.LIKE.getDbView());
        } else throw new NotFoundParameterException("bad id");
    }

    public void createReviewDislike(Integer reviewId, Integer userId) throws NotFoundParameterException {
        if (checkID(reviewId) && checkID(userId)) throw new NotFoundParameterException("bad id");

        Optional<Review> review = reviewStorage.getReview(reviewId);
        Optional<User> user = userStorage.getUser(userId);

        if (review.isPresent() && user.isPresent()) {
            reviewStorage.createReviewLikeDislike(reviewId, userId, LikeValue.DISLIKE.getDbView());
        } else throw new NotFoundParameterException("bad id");
    }

    public void deleteReviewLikeDislike(Integer reviewId, Integer userId) throws NotFoundParameterException {
        if (checkID(reviewId) && checkID(userId)) throw new NotFoundParameterException("bad id");

        Optional<Review> review = reviewStorage.getReview(reviewId);
        Optional<User> user = userStorage.getUser(userId);

        if (review.isPresent() && user.isPresent()) {
            reviewStorage.deleteReviewLikeDislike(reviewId, userId);
        } else throw new NotFoundParameterException("bad id");
    }
}

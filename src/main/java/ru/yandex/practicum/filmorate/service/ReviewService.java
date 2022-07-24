package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.exceptions.CreatingException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.exceptions.UpdateException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewStorage reviewStorage;
    private final UserStorage userStorage;


    public Review createReview(Review review) throws CreatingException, NotFoundParameterException, ValidationException {
        if ( review.getContent().isBlank()||
                review.getFilmId() < 0 ||
                review.getUserId() < 0) {
            throw new NotFoundParameterException("Bad id or content");
        } else if (review.getIsPositive() == null) {
            throw new CreatingException("Bad type or no type");
        }
        return reviewStorage.createReview(review);
    }

    public Review updateReview(Review review) throws NotFoundParameterException, UpdateException {
        if (!reviewStorage.findAll().contains(review))
            throw new UpdateException("Bad review");
        return reviewStorage.updateReview(review);
    }

    public void deleteReview(@PathVariable Integer id) {
        reviewStorage.deleteReview(id);
    }

    public Review getReview(@PathVariable Integer id) throws NotFoundParameterException {
        return reviewStorage.getReview(id).orElseThrow(() -> new NotFoundParameterException("No Review With Such Id"));
    }

    public Collection<Review> findAll() {
        return reviewStorage.findAll();
    }

    public Collection<Review> getTopReviewsByUseful(Collection<Review> reviews, Integer filmId, Integer count) {
        return reviews.stream()
               //   .sorted(Comparator.comparingInt(f -> f.getLikes().size() * (-1)))
                .limit(count)
                .collect(Collectors.toList());
    }

    private boolean checkID(Integer id) {
        return (id == null || id < 0);
    }

    public void addReviewLike(Integer reviewId, Integer userId) throws NotFoundParameterException {
        if (checkID(reviewId) && checkID(userId)) throw new NotFoundParameterException("bad id");

        Optional<Review> review = reviewStorage.getReview(reviewId);
        Optional<User> user = userStorage.getUser(userId);

        if (review.isPresent() && user.isPresent()) {
            review.get().addReviewLike(user.get().getId());
            reviewStorage.createReviewLike(reviewId, userId);
        }
    }

    public void addReviewDislike(Integer reviewId, Integer userId) throws NotFoundParameterException {
        if (checkID(reviewId) && checkID(userId)) throw new NotFoundParameterException("bad id");

        Optional<Review> review = reviewStorage.getReview(reviewId);
        Optional<User> user = userStorage.getUser(userId);

        if (review.isPresent() && user.isPresent()) {
            review.get().addReviewDislike(user.get().getId());
            reviewStorage.createReviewDislike(reviewId, userId);
        }
    }

    public void deleteReviewLike(Integer reviewId, Integer userId) throws NotFoundParameterException {
        if (checkID(reviewId) && checkID(userId)) throw new NotFoundParameterException("bad id");

        Optional<Review> review = reviewStorage.getReview(reviewId);
        Optional<User> user = userStorage.getUser(userId);

        if (review.isPresent() && user.isPresent()) {
            review.get().deleteReviewLike(user.get().getId());
            reviewStorage.deleteReviewLike(reviewId, userId);
        }
    }

    public void deleteReviewDislike(Integer reviewId, Integer userId) throws NotFoundParameterException {
        if (checkID(reviewId) && checkID(userId)) throw new NotFoundParameterException("bad id");

        Optional<Review> review = reviewStorage.getReview(reviewId);
        Optional<User> user = userStorage.getUser(userId);

        if (review.isPresent() && user.isPresent()) {
            review.get().deleteReviewDislike(user.get().getId());
            reviewStorage.deleteReviewDislike(reviewId, userId);
        }
    }

}

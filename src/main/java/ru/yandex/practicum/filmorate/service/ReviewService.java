package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.exceptions.CreatingException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.exceptions.UpdateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewStorage reviewStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;


    public Review create(Review review) throws CreatingException {
        if (review.getContent().isBlank() || review.getFilmId() < 0 || review.getUserId() < 0)
            throw new CreatingException("Bad name");
        return reviewStorage.createReview(review);
    }

    public Review update(Review review) throws NotFoundParameterException, UpdateException {
        if (!reviewStorage.findAll().contains(review))
            throw new UpdateException("Bad review");
        return reviewStorage.update(review);
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
                //        .sorted(Comparator.comparingInt(f -> f.getLikes().size() * (-1)))
                .limit(count)
                .collect(Collectors.toList());
    }

    private boolean checkID(Integer id) {
        return (id == null || id < 0);
    }

    public void addLike(Integer id, Integer userId) throws NotFoundParameterException {
        if (checkID(id))
            throw new NotFoundParameterException("bad id");
        if (checkID(userId))
            throw new NotFoundParameterException("bad id");

        Optional<Film> film = filmStorage.getFilm(id);
        Optional<User> user = userStorage.getUser(userId);

        if (film.isPresent() && user.isPresent()) {
            film.get().addLike(user.get().getId());
            filmStorage.createLike(id, userId);
        }
    }

    public void addDislike(Integer id, Integer userId) throws NotFoundParameterException {
        if (checkID(id))
            throw new NotFoundParameterException("bad id");
        if (checkID(userId))
            throw new NotFoundParameterException("bad id");

        Optional<Film> film = filmStorage.getFilm(id);
        Optional<User> user = userStorage.getUser(userId);

        if (film.isPresent() && user.isPresent()) {
            film.get().addLike(user.get().getId());
            filmStorage.createLike(id, userId);
        }
    }

    public void deleteLike(Integer id, Integer userId) throws NotFoundParameterException {
        if (checkID(id))
            throw new NotFoundParameterException("bad id");
        if (checkID(userId))
            throw new NotFoundParameterException("bad id");

        Optional<Film> film = filmStorage.getFilm(id);
        Optional<User> user = userStorage.getUser(userId);

        if (film.isPresent() && user.isPresent()) {
            film.get().deleteLike(user.get().getId());
            filmStorage.removeLike(id, userId);
        }
    }

    public void deleteDislike(Integer id, Integer userId) throws NotFoundParameterException {
        if (checkID(id))
            throw new NotFoundParameterException("bad id");
        if (checkID(userId))
            throw new NotFoundParameterException("bad id");

        Optional<Film> film = filmStorage.getFilm(id);
        Optional<User> user = userStorage.getUser(userId);

        if (film.isPresent() && user.isPresent()) {
            film.get().deleteLike(user.get().getId());
            filmStorage.removeLike(id, userId);
        }
    }

}

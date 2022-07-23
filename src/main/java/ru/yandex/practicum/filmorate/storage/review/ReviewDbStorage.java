package ru.yandex.practicum.filmorate.storage.review;


import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;
import java.util.Optional;

@Repository
@Slf4j

public class ReviewDbStorage implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;

    public ReviewDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Collection<Review> findAll() {
        return null;
    }

    @Override
    public Review createReview(Review review) {
        String sqlQuery = "INSERT INTO REVIEWS (DIRECTOR_NAME) VALUES (?)";
        //   jdbcTemplate.update(sqlQuery, review.getName());
        // review.setId(setID());
        log.debug("added: {}", review);
        return review;

    }

    @Override
    public Review updateReview(Review review) throws NotFoundParameterException {
        return null;
    }

    @Override
    public boolean deleteReview(Integer reviewId) {
        return false;
    }

    @Override
    public Optional<Review> getReview(Integer id) throws NotFoundParameterException {
        return Optional.empty();
    }

    @Override
    public void createReviewLike(Integer filmID, Integer userID) {

    }

    @Override
    public void deleteReviewLike(Integer filmID, Integer userID) {

    }

    @Override
    public void createReviewDislike(Integer filmID, Integer userID) {

    }

    @Override
    public void deleteReviewDislike(Integer filmID, Integer userID) {

    }
}

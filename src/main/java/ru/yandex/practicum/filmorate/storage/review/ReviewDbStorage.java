package ru.yandex.practicum.filmorate.storage.review;


import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Repository
@Slf4j

public class ReviewDbStorage implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;

    public ReviewDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    private Review makeReview(ResultSet rs) throws SQLException {
        return new Review(
                rs.getInt("review_id"),
                rs.getString("content"),
                rs.getBoolean("is_positive"),
                rs.getInt("user_id"),
                rs.getInt("film_id")
        );
    }

    @Override
    public Collection<Review> findAll() {
        String sql = "SELECT * FROM REVIEWS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeReview(rs));
    }

    @Override
    public Review createReview(Review review) throws ValidationException, NotFoundParameterException {
        if (findAll().contains(review)

        ) {
            log.debug("Oh, no. validation failed");
            throw new ValidationException("oh, something was wrong");
        } else {
            String sqlQuery = "INSERT INTO REVIEWS (CONTENT, IS_POSITIVE, USER_ID, FILM_ID)" +
                    " VALUES (?, ?, ?, ?)";

            jdbcTemplate.update(sqlQuery, review.getContent(), review.getIsPositive(),
                    review.getUserId(), review.getFilmId());
            setID(review);

            log.debug("added: {}", review);
            //  return  getReview(review.getId()).get();
            return review;
        }
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
        SqlRowSet reviewRows = jdbcTemplate.queryForRowSet("SELECT * FROM REVIEWS WHERE REVIEW_ID = ?", id);


        if (reviewRows.next()) {
            Review review = new Review(
                    reviewRows.getInt("REVIEW_ID"),
                    reviewRows.getString("content"),
                    reviewRows.getBoolean("is_positive"),
                    reviewRows.getInt("user_id"),
                    reviewRows.getInt("film_id")

            );

            log.info("Найден отзыв: {}", review.getReviewId());

            return Optional.of(review);
        } else {
            log.info("Отзыв с идентификатором {} не найден.", id);
            return Optional.empty();
        }
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

    private void setID(Review review) {
        for (Review r : findAll()) {
            if (r.equals(review))
                review.setReviewId(r.getReviewId());
        }
    }

}

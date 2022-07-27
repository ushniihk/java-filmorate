package ru.yandex.practicum.filmorate.storage.review;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.EventOperations;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.Event.EventStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ReviewDbStorage implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;
    private final EventStorage eventStorage;

    private Review make(ResultSet rs) throws SQLException {
        Integer useful = useful(rs.getInt("review_id"));
        Review review = new Review(
                rs.getInt("review_id"),
                rs.getString("content"),
                rs.getBoolean("is_positive"),
                rs.getInt("user_id"),
                rs.getInt("film_id")
        );
        review.setUseful(useful);
        return review;
    }

    @Override
    public Collection<Review> findAll() {
        String sql = "SELECT * FROM REVIEWS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> make(rs));
    }

    @Override
    public Review create(Review review) throws ValidationException {
        String sqlQuery = "INSERT INTO REVIEWS (CONTENT, IS_POSITIVE, USER_ID, FILM_ID)" +
                " VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery, review.getContent(), review.getIsPositive(),
                review.getUserId(), review.getFilmId());
        setID(review);
        log.debug("added: {}", review);
        eventStorage.add(review.getUserId(), review.getReviewId(), EventType.REVIEW, EventOperations.ADD);
        return review;
    }

    @Override
    public Review update(Review review) {
        String sqlQuery = "update REVIEWS set CONTENT = ?, IS_POSITIVE = ? where REVIEW_ID = ?";
        jdbcTemplate.update(sqlQuery, review.getContent(), review.getIsPositive(), review.getReviewId());
        Review review1 = get(review.getReviewId()).orElseThrow();
        eventStorage.add(review1.getUserId(), review.getReviewId(), EventType.REVIEW, EventOperations.UPDATE);
        return get(review.getReviewId()).orElse(null);
    }

    @Override
    public void delete(Integer reviewId) {
        Optional<Review> review = get(reviewId);
        review.ifPresent(value -> eventStorage.add(value.getUserId(), value.getReviewId(), EventType.REVIEW, EventOperations.REMOVE));
        String sqlQuery = "DELETE FROM REVIEWS WHERE REVIEW_ID = ?";
        jdbcTemplate.update(sqlQuery, reviewId);
    }

    private Integer useful(Integer id) {
        String sql = "SELECT SUM (LIKE_TYPE) from REVIEW_LIKE Where REVIEW_ID = ?";
        Integer useful = jdbcTemplate.queryForObject(sql, Integer.class, id);
        if (useful != null) {
            return useful;
        } else return 0;
    }

    @Override
    public Optional<Review> get(Integer id) {
        SqlRowSet reviewRows = jdbcTemplate.queryForRowSet("SELECT * FROM REVIEWS WHERE REVIEW_ID = ?", id);
        Integer useful = useful(id);
        if (reviewRows.next()) {
            Review review = new Review(
                    reviewRows.getInt("REVIEW_ID"),
                    reviewRows.getString("content"),
                    reviewRows.getBoolean("is_positive"),
                    reviewRows.getInt("user_id"),
                    reviewRows.getInt("film_id"));
            log.info("Найден отзыв: {}", review.getReviewId());
            review.setUseful(useful);
            return Optional.of(review);
        } else {
            log.info("Отзыв с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    @Override
    public void createLikeDislike(Integer reviewId, Integer userID, Integer value) {
        String sqlQuery = "INSERT INTO REVIEW_LIKE (REVIEW_ID, USER_ID, LIKE_TYPE)" +
                " VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, reviewId, userID, value);
    }

    @Override
    public void deleteLikeDislike(Integer reviewId, Integer userID) {
        String sqlQuery = "DELETE FROM REVIEWS WHERE REVIEW_ID =? AND USER_ID= ?";
        jdbcTemplate.update(sqlQuery, reviewId);
    }

    private void setID(Review review) {
        for (Review r : findAll()) {
            if (r.equals(review))
                review.setReviewId(r.getReviewId());
        }
    }
}
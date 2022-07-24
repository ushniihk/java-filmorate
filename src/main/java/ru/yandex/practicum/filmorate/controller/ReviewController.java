package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.Collection;


@RestController
@Slf4j
@Data
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public Review create(@RequestBody Review review) throws CreatingException, NotFoundParameterException, ValidationException {
        return reviewService.createReview(review);
    }

    @PutMapping
    public Review update(@RequestBody Review review) throws UpdateException, NotFoundParameterException {
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Integer id) {
        reviewService.deleteReview(id);
    }

    @GetMapping("/{id}")
    public Review getReview(@PathVariable Integer id) throws NotFoundParameterException {
        return reviewService.getReview(id);
    }

    @GetMapping
    public Collection<Review> getTopReviews(
            @RequestParam(required = false) Integer filmId,
            @RequestParam(required = false, defaultValue = "10") Integer count) throws IncorrectParameterException {
        if (count <= 0) throw new IncorrectParameterException("Bad count");
        if (filmId == null) {
            return reviewService.findAll();
        } else {
            return reviewService.getTopReviewsByUseful(reviewService.findAll(), filmId, count);
        }
    }

    @PutMapping("/{id}/like/{userId}")
    public void userLikesTheReview(@PathVariable Integer id, @PathVariable Integer userId) throws NotFoundParameterException {
        reviewService.addReviewLike(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void userDislikesTheReview(@PathVariable Integer id, @PathVariable Integer userId) throws NotFoundParameterException {
        reviewService.addReviewDislike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void userDeleteLike(@PathVariable Integer id, @PathVariable Integer userId) throws NotFoundParameterException {
        reviewService.deleteReviewLike(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void userDeleteDislike(@PathVariable Integer id, @PathVariable Integer userId) throws NotFoundParameterException {
        reviewService.deleteReviewDislike(id, userId);
    }

}



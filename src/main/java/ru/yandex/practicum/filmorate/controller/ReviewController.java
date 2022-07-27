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
        return reviewService.create(review);
    }

    @PutMapping
    public Review update(@RequestBody Review review) throws UpdateException, NotFoundParameterException, ValidationException, CreatingException {
        return reviewService.update(review);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        reviewService.delete(id);
    }

    @GetMapping("/{id}")
    public Review get(@PathVariable Integer id) throws NotFoundParameterException {
        return reviewService.get(id);
    }

    @GetMapping
    public Collection<Review> getTop(
            @RequestParam(required = false) Integer filmId,
            @RequestParam(required = false, defaultValue = "10") Integer count) throws IncorrectParameterException {
        if (filmId == null) {
            return reviewService.findAll();
        } else {
            return reviewService.getTopByUseful(reviewService.findAll(), filmId, count);
        }
    }

    @PutMapping("/{id}/like/{userId}")
    public void userLikesTheReview(@PathVariable Integer id, @PathVariable Integer userId) throws NotFoundParameterException {
        reviewService.createLike(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void userDislikesTheReview(@PathVariable Integer id, @PathVariable Integer userId) throws NotFoundParameterException {
        reviewService.createDislike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void userDeleteLike(@PathVariable Integer id, @PathVariable Integer userId) throws NotFoundParameterException {
        reviewService.deleteLikeDislike(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void userDeleteDislike(@PathVariable Integer id, @PathVariable Integer userId) throws NotFoundParameterException {
        reviewService.deleteLikeDislike(id, userId);
    }

}



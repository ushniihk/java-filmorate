package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.CreatingException;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundParameterException;
import ru.yandex.practicum.filmorate.exceptions.UpdateException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.Collection;


@RestController
@Slf4j
@Data
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public Review create(@RequestBody Review review) throws CreatingException {
        //   return reviewService.create(review);
        return review;
    }

    @PutMapping
    public Review update(@RequestBody Review review) throws UpdateException {
        // return reviewService.update(review);
        return review;
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Integer id) {
        //  reviewService.deleteReview(id);
    }

    @GetMapping("/{id}")
    public Review getReview(@PathVariable Integer id) throws NotFoundParameterException {
        //    return reviewService.getReview(id);
        return null;
    }


    @GetMapping//дописать логику
    public Collection<Review> getTopReviews(@RequestParam(required = false) Integer filmId,
                                            @RequestParam(required = false, defaultValue = "10") Integer count) throws IncorrectParameterException {
        {
            if (count <= 0)
                throw new IncorrectParameterException("count");
            return null;
            // return reviewService.getTopReviewsByUseful(reviewService.findAll(), count);
        }
    }}



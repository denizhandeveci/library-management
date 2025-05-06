package com.example.library.management.controller;

import com.example.library.management.entity.ReviewEntity;
import com.example.library.management.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService){
        this.reviewService = reviewService;
    }

    @PostMapping("/add-review")
    public ReviewEntity addReview(@RequestBody ReviewEntity reviewEntity){
        return reviewService.addReview(reviewEntity);
    }
    @PostMapping("/add-multiple-reviews")
    public List<ReviewEntity> addMultipleReviews(@RequestBody List<ReviewEntity> reviewEntityList){
        return reviewService.addMultipleReviews(reviewEntityList);
    }
    @GetMapping("/get-reviews/{bookId}")
    public String getReviewsForBook(@PathVariable Long bookId){
        return reviewService.getReviewsForBook(bookId);
    }
    @GetMapping("/get-average-rating/{bookId}")
    public String getAverageRating(@PathVariable Long bookId){
        return reviewService.getAverageRating(bookId);
    }

}

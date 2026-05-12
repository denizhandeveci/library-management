package com.example.library.management.controller;

import com.example.library.management.dto.ReviewRequest;
import com.example.library.management.dto.ReviewResponse;
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

    @GetMapping("/reviews/{bookId}")
    public List<ReviewResponse> getReviewsForBook(@PathVariable Long bookId){
        return reviewService.getReviewsForBook(bookId);
    }

    @GetMapping("/reviews/rating/{bookId}")
    public String getAverageRating(@PathVariable Long bookId){
        return reviewService.getAverageRating(bookId);
    }

    @PostMapping("/reviews")
    public ReviewResponse addReview(@RequestBody ReviewRequest reviewRequestDTO){
        return reviewService.addReview(reviewRequestDTO);
    }

    @PostMapping("/add-multiple-reviews")
    public List<ReviewResponse> addMultipleReviews(@RequestBody List<ReviewRequest> reviewRequestDTOList){
        return reviewService.addMultipleReviews(reviewRequestDTOList);
    }

    @PostMapping("/reset-reviews")
    public String deleteAllReviewsAndResetAutoIncrement(){
        reviewService.deleteAllReviewsAndResetAutoIncrement();
        return "All the data in reviews DB is deleted and reset";
    }
     @DeleteMapping("/delete-review-by-id/{id}")
    public void deleteReviewById(@PathVariable Long id) {
        reviewService.deleteReviewById(id);
    }

}

package com.example.library.management.controller;

import com.example.library.management.dto.ReviewRequestDTO;
import com.example.library.management.dto.ReviewResponseDTO;
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
    public ReviewResponseDTO addReview(@RequestBody ReviewRequestDTO reviewRequestDTO){
        return reviewService.addReview(reviewRequestDTO);
    }
    @PostMapping("/add-multiple-reviews")
    public List<ReviewResponseDTO> addMultipleReviews(@RequestBody List<ReviewRequestDTO> reviewRequestDTOList){
        return reviewService.addMultipleReviews(reviewRequestDTOList);
    }
    @GetMapping("/get-reviews/{bookId}")
    public List<ReviewResponseDTO> getReviewsForBook(@PathVariable Long bookId){
        return reviewService.getReviewsForBook(bookId);
    }
    @GetMapping("/get-average-rating/{bookId}")
    public String getAverageRating(@PathVariable Long bookId){
        return reviewService.getAverageRating(bookId);
    }
    @DeleteMapping("/delete-review-by-id/{id}")
    public void deleteReviewById(@PathVariable Long id) {
        reviewService.deleteReviewById(id);
    }
    @PostMapping("/reset-reviews")
    public String deleteAllReviewsAndResetAutoIncrement(){
        reviewService.deleteAllReviewsAndResetAutoIncrement();
        return "All the data in reviews DB is deleted and reset";
    }
}

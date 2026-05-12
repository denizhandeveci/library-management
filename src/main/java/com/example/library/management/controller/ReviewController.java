package com.example.library.management.controller;

import com.example.library.management.dto.ReviewRequest;
import com.example.library.management.dto.ReviewResponse;
import com.example.library.management.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReviewController
{
    private static final Logger log = LoggerFactory.getLogger(ReviewController.class);

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/add-review")
    public ReviewResponse addReview(@RequestBody ReviewRequest reviewRequest) {
        log.debug(
                "Received add review request for userId={} and bookId={}",
                reviewRequest.userId(),
                reviewRequest.bookId()
        );

        return reviewService.addReview(reviewRequest);
    }

    @PostMapping("/add-multiple-reviews")
    public List<ReviewResponse> addMultipleReviews(@RequestBody List<ReviewRequest> reviewRequests) {
        log.debug("Received add multiple reviews request with reviewCount={}", reviewRequests.size());

        return reviewService.addMultipleReviews(reviewRequests);
    }

    @GetMapping("/get-reviews/{bookId}")
    public List<ReviewResponse> getReviewsForBook(@PathVariable Long bookId) {
        log.debug("Received get reviews request for bookId={}", bookId);

        return reviewService.getReviewsForBook(bookId);
    }

    @GetMapping("/get-average-rating/{bookId}")
    public String getAverageRating(@PathVariable Long bookId) {
        log.debug("Received get average rating request for bookId={}", bookId);

        return reviewService.getAverageRating(bookId);
    }

    @DeleteMapping("/delete-review-by-id/{id}")
    public void deleteReviewById(@PathVariable Long id) {
        log.debug("Received delete review request for reviewId={}", id);

        reviewService.deleteReviewById(id);
    }

    @PostMapping("/reset-reviews")
    public ResetReviewsResponse deleteAllReviewsAndResetAutoIncrement() {
        log.warn("Received reset reviews request");

        reviewService.deleteAllReviewsAndResetAutoIncrement();

        return new ResetReviewsResponse("All the data in reviews DB is deleted and reset");
    }

    public record ResetReviewsResponse(
            String message
    ) {}
}

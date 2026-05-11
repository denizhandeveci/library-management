package com.example.library.management.service;

import com.example.library.management.dto.ReviewRequest;
import com.example.library.management.dto.ReviewResponse;
import com.example.library.management.entity.Book;
import com.example.library.management.entity.Review;
import com.example.library.management.entity.User;
import com.example.library.management.repository.BookRepository;
import com.example.library.management.repository.ReviewRepository;
import com.example.library.management.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService
{
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public ReviewResponse addReview(ReviewRequest reviewRequest) {
        Book book = bookRepository.findById(reviewRequest.bookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        User user = userRepository.findById(reviewRequest.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Review savedReview = reviewRepository.save(createReviewEntity(book, user, reviewRequest));

        return ReviewResponse.fromEntity(savedReview);
    }

    public List<ReviewResponse> addMultipleReviews(List<ReviewRequest> reviewRequests) {
        List<Review> reviews = reviewRequests.stream()
                .map(reviewRequest -> {
                    Book book = bookRepository.findById(reviewRequest.bookId())
                            .orElseThrow(() -> new RuntimeException("Book not found"));
                    User user = userRepository.findById(reviewRequest.userId())
                            .orElseThrow(() -> new RuntimeException("User not found"));

                    return createReviewEntity(book, user, reviewRequest);
                })
                .toList();

        List<Review> savedReviews = reviewRepository.saveAll(reviews);

        return savedReviews.stream()
                .map(ReviewResponse::fromEntity)
                .toList();
    }

    public List<ReviewResponse> getReviewsForBook(Long bookId) {
        return reviewRepository.findByBookId(bookId)
                .stream()
                .map(ReviewResponse::fromEntity)
                .toList();
    }

    public String getAverageRating(Long bookId) {
        double totalRatingPoints = 0;
        Book book = bookRepository.findById(bookId).orElse(null);

        if (book == null) {
            return "Book with Id: " + bookId + " is not found";
        }

        List<Review> reviewEntityList = reviewRepository.findByBookId(bookId);
        if (reviewEntityList.isEmpty()) {
            return "No reviews available for this book.";
        }

        for (Review review : reviewEntityList) {
            totalRatingPoints = totalRatingPoints + review.rating;
        }

        double averageReviewPoints = totalRatingPoints / reviewEntityList.size();

        // TODO backend should return a structured response, frontend should display it however it likes, yb
        return "The average ratings for " + "'" + book.title + "' is " + averageReviewPoints;
    }

    public void deleteReviewById(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new RuntimeException("Review with id " + reviewId + " does not exist.");
        }
        reviewRepository.deleteById(reviewId);
    }

    @Transactional
    public void deleteAllReviewsAndResetAutoIncrement() {
        reviewRepository.deleteAll();
        reviewRepository.resetAutoIncrement();
    }

    public Review createReviewEntity(Book book, User user, ReviewRequest reviewRequest) {
        Review reviewEntity = new Review();

        reviewEntity.rating = reviewRequest.rating();
        reviewEntity.comment = reviewRequest.comment();

        reviewEntity.book = book;
        reviewEntity.user = user;

        reviewEntity.createdAt = LocalDateTime.now();

        return reviewEntity;
    }

}

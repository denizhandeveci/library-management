package com.example.library.management.service;

import com.example.library.management.dto.ReviewRequest;
import com.example.library.management.dto.ReviewResponse;
import com.example.library.management.entity.Book;
import com.example.library.management.entity.Review;
import com.example.library.management.entity.User;
import com.example.library.management.exception.ResourceNotFoundException;
import com.example.library.management.repository.BookRepository;
import com.example.library.management.repository.ReviewRepository;
import com.example.library.management.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService
{
    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);

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
        log.info(
                "Creating review for userId={} and bookId={} with rating={}",
                reviewRequest.userId(),
                reviewRequest.bookId(),
                reviewRequest.rating()
        );

        Book book = bookRepository.findById(reviewRequest.bookId())
                .orElseThrow(() -> {
                    log.warn("Review creation failed because bookId={} was not found", reviewRequest.bookId());
                    return ResourceNotFoundException.forId("Book", reviewRequest.bookId());
                });

        User user = userRepository.findById(reviewRequest.userId())
                .orElseThrow(() -> {
                    log.warn("Review creation failed because userId={} was not found", reviewRequest.userId());
                    return ResourceNotFoundException.forId("User", reviewRequest.userId());
                });

        Review savedReview = reviewRepository.save(createReviewEntity(book, user, reviewRequest));

        log.info(
                "Review created successfully with reviewId={} for userId={} and bookId={}",
                savedReview.id,
                user.id,
                book.id
        );

        return ReviewResponse.fromEntity(savedReview);
    }

    public List<ReviewResponse> addMultipleReviews(List<ReviewRequest> reviewRequests) {
        log.info("Creating multiple reviews with reviewCount={}", reviewRequests.size());

        List<Review> reviews = reviewRequests.stream()
                .map(reviewRequest -> {
                    Book book = bookRepository.findById(reviewRequest.bookId())
                            .orElseThrow(() -> {
                                log.warn("Bulk review creation failed because bookId={} was not found", reviewRequest.bookId());
                                return ResourceNotFoundException.forId("Book", reviewRequest.bookId());
                            });

                    User user = userRepository.findById(reviewRequest.userId())
                            .orElseThrow(() -> {
                                log.warn("Bulk review creation failed because userId={} was not found", reviewRequest.userId());
                                return ResourceNotFoundException.forId("User", reviewRequest.userId());
                            });

                    return createReviewEntity(book, user, reviewRequest);
                })
                .toList();

        List<Review> savedReviews = reviewRepository.saveAll(reviews);

        log.info("Created {} reviews successfully", savedReviews.size());

        return savedReviews.stream()
                .map(ReviewResponse::fromEntity)
                .toList();
    }

    public List<ReviewResponse> getReviewsForBook(Long bookId) {
        List<ReviewResponse> reviews = reviewRepository.findByBookId(bookId)
                .stream()
                .map(ReviewResponse::fromEntity)
                .toList();

        log.debug("Found {} reviews for bookId={}", reviews.size(), bookId);

        return reviews;
    }

    public String getAverageRating(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Book", bookId));

        List<Review> reviews = reviewRepository.findByBookId(bookId);
        if (reviews.isEmpty()) {
            return "No reviews available for this book.";
        }

        var averageRating = reviews.stream()
                .mapToInt(review -> review.rating)
                .average()
                .orElse(0);

        log.debug(
                "Calculated average rating={} from reviewCount={} for bookId={}",
                averageRating,
                reviews.size(),
                bookId
        );

        // TODO backend should return a structured response, frontend should display it however it likes, yb
        return "The average ratings for " + "'" + book.title + "' is " + averageRating;
    }

    public void deleteReviewById(Long reviewId) {
        log.info("Deleting review with reviewId={}", reviewId);

        if (!reviewRepository.existsById(reviewId)) {
            log.warn("Review deletion failed because reviewId={} does not exist", reviewId);

            throw ResourceNotFoundException.forId("Review", reviewId);
        }
        reviewRepository.deleteById(reviewId);

        log.info("Review deleted successfully with reviewId={}", reviewId);
    }

    @Transactional
    public void deleteAllReviewsAndResetAutoIncrement() {
        log.warn("Deleting all reviews and resetting auto increment");

        reviewRepository.deleteAll();
        reviewRepository.resetAutoIncrement();

        log.warn("All reviews deleted and auto increment reset");
    }

    public Review createReviewEntity(Book book, User user, ReviewRequest reviewRequest) {
        Review reviewEntity = new Review();

        reviewEntity.rating = reviewRequest.rating();
        reviewEntity.comment = reviewRequest.comment();

        reviewEntity.book = book;
        reviewEntity.user = user;

        return reviewEntity;
    }

}

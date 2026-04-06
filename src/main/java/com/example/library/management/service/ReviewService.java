package com.example.library.management.service;

import com.example.library.management.entity.BookEntity;
import com.example.library.management.entity.ReviewEntity;
import com.example.library.management.repository.BookRepository;
import com.example.library.management.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
    }

    public ReviewEntity addReview(ReviewEntity reviewEntity) {
        reviewEntity.setCreatedAt(LocalDateTime.now());
        return reviewRepository.save(reviewEntity);
    }
    public List<ReviewEntity> addMultipleReviews(List<ReviewEntity> reviewEntityList){
        for(ReviewEntity reviewEntity:reviewEntityList){
            reviewEntity.setCreatedAt(LocalDateTime.now());
        }
        return reviewRepository.saveAll(reviewEntityList);
    }

    public String getReviewsForBook(Long bookId) {
        List<ReviewEntity> reviewEntityList = reviewRepository.findByBookEntityId(bookId);
        StringBuilder sb = new StringBuilder();
        for (ReviewEntity review : reviewEntityList) {
            sb.append(review.toString()).append("\n");
        }
        return sb.toString();
    }

    public String getAverageRating(Long bookId) {
        double totalRatingPoints=0;
        BookEntity bookEntity = bookRepository.findById(bookId).get();
        List<ReviewEntity> reviewEntityList = reviewRepository.findByBookEntityId(bookId);
        if (reviewEntityList.isEmpty()) {
            return "No reviews available for this book.";
        }
        for(ReviewEntity review:reviewEntityList){
            totalRatingPoints = totalRatingPoints + review.getRating();
        }

        double averageReviewPoints = totalRatingPoints/reviewEntityList.size();
        return "The average ratings for " + "'" + bookEntity.getTitle() +"' is " + averageReviewPoints ;
    }

    public void deleteReviewById(Long reviewId){
        if(!reviewRepository.existsById(reviewId)){
            throw new RuntimeException("Review with id " + reviewId + " does not exist.");
        }
        reviewRepository.deleteById(reviewId);
    }
    @Transactional
    public void deleteAllReviewsAndResetAutoIncrement(){
        reviewRepository.deleteAll();
        reviewRepository.resetAutoIncrement();
    }

}

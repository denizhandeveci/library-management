package com.example.library.management.service;

import com.example.library.management.dto.ReviewRequestDTO;
import com.example.library.management.dto.ReviewResponseDTO;
import com.example.library.management.entity.Book;
import com.example.library.management.entity.ReviewEntity;
import com.example.library.management.entity.UserEntity;
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

    public ReviewResponseDTO addReview(ReviewRequestDTO reviewRequestDTO) {
        ReviewEntity reviewEntity = mapTOEntity(reviewRequestDTO);
        ReviewEntity savedReview = reviewRepository.save(reviewEntity);
        return mapToDTO(savedReview);
    }

    public List<ReviewResponseDTO> addMultipleReviews(List<ReviewRequestDTO> reviewRequestDTOList) {
        List<ReviewEntity> reviewEntityList = reviewRequestDTOList.stream()
                .map(this::mapTOEntity)
                .toList();
        List<ReviewEntity> savedReviews = reviewRepository.saveAll(reviewEntityList);
        return savedReviews.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<ReviewResponseDTO> getReviewsForBook(Long bookId) {
        return reviewRepository.findByBookEntityId(bookId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public String getAverageRating(Long bookId) {
        double totalRatingPoints = 0;
        Book book = bookRepository.findById(bookId).orElse(null);

        if (book == null) {
            return "Book with Id: " + bookId + " is not found";
        }

        List<ReviewEntity> reviewEntityList = reviewRepository.findByBookEntityId(bookId);
        if (reviewEntityList.isEmpty()) {
            return "No reviews available for this book.";
        }
        for (ReviewEntity review : reviewEntityList) {
            totalRatingPoints = totalRatingPoints + review.getRating();
        }

        double averageReviewPoints = totalRatingPoints / reviewEntityList.size();
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

    public ReviewResponseDTO mapToDTO(ReviewEntity reviewEntity) {
        ReviewResponseDTO reviewResponseDTO = new ReviewResponseDTO();

        reviewResponseDTO.setId(reviewEntity.getId());
        reviewResponseDTO.setComment(reviewEntity.getComment());
        reviewResponseDTO.setRating(reviewEntity.getRating());
        reviewResponseDTO.setBookId(reviewEntity.getBookEntity().id);
        reviewResponseDTO.setBookTitle(reviewEntity.getBookEntity().title);
        reviewResponseDTO.setUserId(reviewEntity.getUserEntity().getId());
        reviewResponseDTO.setUserName(reviewEntity.getUserEntity().getName());
        reviewResponseDTO.setCreatedAt(reviewEntity.getCreatedAt());

        return reviewResponseDTO;

    }

    public ReviewEntity mapTOEntity(ReviewRequestDTO reviewRequestDTO) {
        Book book = bookRepository.findById(reviewRequestDTO.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        UserEntity user = userRepository.findById(reviewRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ReviewEntity reviewEntity = new ReviewEntity();

        reviewEntity.setRating(reviewRequestDTO.getRating());
        reviewEntity.setComment(reviewRequestDTO.getComment());

        reviewEntity.setBookEntity(book);
        reviewEntity.setUserEntity(user);

        reviewEntity.setCreatedAt(LocalDateTime.now());

        return reviewEntity;
    }

}

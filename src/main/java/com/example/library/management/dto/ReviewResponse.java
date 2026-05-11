package com.example.library.management.dto;

import com.example.library.management.entity.Review;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,

        int rating,
        String comment,

        Long bookId,
        String bookTitle,

        Long userId,
        String userName,

        LocalDateTime createdAt
)
{
    public static ReviewResponse fromEntity(Review review) {
        var book = review.book;
        var user = review.user;

        return new ReviewResponse(
                review.id,

                review.rating,
                review.comment,

                book.id,
                book.title,

                user.getId(),
                user.getName(),

                review.createdAt
        );
    }
}

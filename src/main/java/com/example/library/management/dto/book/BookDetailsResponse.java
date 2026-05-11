package com.example.library.management.dto.book;

import com.example.library.management.entity.Book;

import java.util.List;

public record BookDetailsResponse(
        BookResponse book,
        List<BookRecommendation> recommendationsByGenre,
        List<BookRecommendation> recommendationsByAuthor
)
{
    public record BookRecommendation(
            Long id,
            String title,
            String author,
            String genre
    )
    {
        public static BookRecommendation fromEntity(Book book) {
            return new BookRecommendation(
                    book.id,
                    book.title,
                    book.author,
                    book.genre
            );
        }
    }
}

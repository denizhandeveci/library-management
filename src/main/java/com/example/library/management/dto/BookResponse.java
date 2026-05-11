package com.example.library.management.dto;

import com.example.library.management.entity.Book;

public record BookResponse(
        Long id,
        String title,
        String author,
        String isbn,
        String genre,

        Integer numOfTotalCopies,
        Integer numOfCopiesAvailable,

        Boolean available,

        String coverImageUrl
) {
    public static BookResponse fromEntity(Book book) {
        return new BookResponse(
                book.id,
                book.title,
                book.author,
                book.isbn,
                book.genre,
                book.numOfTotalCopies,
                book.numOfCopiesAvailable,
                book.available,
                book.coverImageUrl
        );
    }
}

package com.example.library.management.dto;

import com.example.library.management.entity.Book;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BookRequest(
        @NotBlank
        String title,

        @NotBlank
        String author,

        @NotBlank
        String isbn,

        String genre,

        @NotNull
        @Positive
        Integer numOfTotalCopies,

        String coverImageUrl
)
{
    public Book toEntity() {
        Book book = new Book();

        book.title = title();
        book.author = author();
        book.isbn = isbn();
        book.genre = genre();
        book.numOfTotalCopies = numOfTotalCopies();
        book.numOfCopiesAvailable = numOfTotalCopies();
        book.available = true;

        return book;
    }
}
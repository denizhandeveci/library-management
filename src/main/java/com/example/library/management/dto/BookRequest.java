package com.example.library.management.dto;

import com.example.library.management.entity.Book;

public record BookRequest(
        String title,
        String author,
        String isbn,
        String genre,
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
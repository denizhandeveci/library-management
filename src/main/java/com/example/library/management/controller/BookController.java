package com.example.library.management.controller;

import com.example.library.management.dto.book.BookRequest;
import com.example.library.management.dto.book.BookResponse;
import com.example.library.management.dto.book.BookSortField;
import com.example.library.management.service.BookService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookController
{

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public List<BookResponse> getBooks(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "AUTHOR") BookSortField sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction
    )
    {
        return bookService.getBooks(title, sortBy, direction);
    }

    @GetMapping("/books/{id}")
    public String viewBook(@PathVariable Long id) {
        return bookService.viewBook(id);
    }

    @PostMapping("/books")
    public ResponseEntity<BookResponse> createBook(@RequestBody @Valid BookRequest bookRequest) {

        BookResponse saved = bookService.createBook(bookRequest);

        return ResponseEntity.ok(saved);
    }

    @PutMapping("/books/{bookId}")
    @Transactional
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable Long bookId,
            @RequestBody @Valid BookRequest bookRequest
    )
    {
        return bookService.updateBook(bookId, bookRequest);
    }

    @DeleteMapping("/books/{bookId}")
    public void deleteBookById(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
    }

    @PostMapping("/reset-library")
    public String deleteAllBooksAndResetAutoIncrement() {
        bookService.deleteAllBooksAndResetAutoIncrement();
        return "All the data in library is deleted and reset";
    }
}

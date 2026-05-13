package com.example.library.management.controller;

import com.example.library.management.dto.book.BookDetailsResponse;
import com.example.library.management.dto.book.BookRequest;
import com.example.library.management.dto.book.BookResponse;
import com.example.library.management.dto.book.BookSortField;
import com.example.library.management.service.BookService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
    private static final Logger log = LoggerFactory.getLogger(BookController.class);

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
        log.debug("Received get books request with title={}, sortBy={}, direction={}", title, sortBy, direction);

        return bookService.getBooks(title, sortBy, direction);
    }

    @GetMapping("/books/{bookId}")
    public BookDetailsResponse getBookDetails(@PathVariable Long bookId) {
        log.debug("Received get book details request for bookId={}", bookId);

        return bookService.getBookDetails(bookId);
    }

    @PostMapping("/books")
    public ResponseEntity<BookResponse> createBook(@RequestBody @Valid BookRequest bookRequest) {
        log.debug("Received create book request for title={} and isbn={}", bookRequest.title(), bookRequest.isbn());

        BookResponse saved = bookService.createBook(bookRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }

    @PutMapping("/books/{bookId}")
    public BookResponse updateBook(@PathVariable Long bookId,
                                    @RequestBody @Valid BookRequest bookRequest){
        log.debug("Received update book request for bookId={}", bookId);
        return bookService.updateBook(bookId, bookRequest);
    }

    @DeleteMapping("/books")
    public ResetLibraryResponse deleteAllBooksAndResetAutoIncrement() {
        log.warn("Received reset library request");
        bookService.deleteAllBooksAndResetAutoIncrement();
        return new ResetLibraryResponse("All the data in library is deleted and reset");
    }

    @DeleteMapping("/books/{bookId}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long bookId) {
        log.debug("Received delete book request for bookId={}", bookId);

        bookService.softDeleteBook(bookId);

        return ResponseEntity.noContent().build();
    }

    public record ResetLibraryResponse(
            String message
    ) {}
}

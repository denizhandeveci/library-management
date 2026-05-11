package com.example.library.management.controller;

import com.example.library.management.dto.BookRequest;
import com.example.library.management.dto.BookResponse;
import com.example.library.management.service.BookService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService){
        this.bookService = bookService;
    }

//    @PostMapping("/create-book")
//    public BookEntity createBook(@RequestBody BookEntity bookEntity){
//        return bookService.createBook(bookEntity);
//    }
    @PutMapping("/update-book/{id}")
    @Transactional
    public ResponseEntity<BookResponse> updateBook(@PathVariable Long id, @RequestBody BookRequest bookRequestDTO){
        return bookService.updateBook(id, bookRequestDTO);
    }

    @PostMapping(value = "/create-book")
    public ResponseEntity<BookResponse> createBook(@RequestBody BookRequest bookRequest)  {

        BookResponse saved = bookService.create(bookRequest);

        return ResponseEntity.ok(saved);
    }
    @PostMapping("/create-multiple-books")
    public List<BookResponse> createMultipleBooks(@RequestBody List<BookRequest> listOfBooks){
        return bookService.createMultipleBooks(listOfBooks);
    }

    @DeleteMapping("/delete-book-by-id/{id}")
    public void deleteBookById(@PathVariable Long id){
        bookService.deleteBookById(id);
    }

    @PostMapping("/reset-library")
    public String deleteAllBooksAndResetAutoIncrement(){
        bookService.deleteAllBooksAndResetAutoIncrement();
        return "All the data in library is deleted and reset";
    }

    @PostMapping("/search-book-by-title/{title}")
    public String searchBookByTitle(@PathVariable  String title){
        return bookService.searchBookByTitle(title);
    }

    @GetMapping("/view-book/{id}")
    public String viewBook(@PathVariable Long id){
        return bookService.viewBook(id);
    }

    @GetMapping("/get-all-books-asc")
    public List<BookResponse> getAllBooksSortedByAuthorAsc() {
        return bookService.getAllBooksSortedByAuthorAsc();
    }

    @GetMapping("/get-all-books")
    public List<BookResponse> getAllBooks(){
        return bookService.getAllBooks();
    }

}

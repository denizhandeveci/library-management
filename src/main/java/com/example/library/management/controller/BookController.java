package com.example.library.management.controller;

import com.example.library.management.entity.BookEntity;
import com.example.library.management.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/create-book")
    public ResponseEntity<BookEntity> createBook(@RequestBody BookEntity bookEntity) {
        BookEntity saved = bookService.createBook(bookEntity);
        return ResponseEntity.ok(saved);
    }


    @DeleteMapping("/delete-book-by-id/{id}")
    public void deleteBookById(@PathVariable Long id){
        bookService.deleteBookById(id);
    }

}

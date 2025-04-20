package com.example.library.management.controller;

import com.example.library.management.entity.BookEntity;
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
    @PostMapping("/create-book")
    public ResponseEntity<BookEntity> createBook(@RequestBody BookEntity bookEntity) {
        BookEntity saved = bookService.createBook(bookEntity);
        return ResponseEntity.ok(saved);
    }
    @PostMapping("/create-multiple-books")
    public List<BookEntity> createMultipleBooks(@RequestBody List<BookEntity> listOfBooks){
        return bookService.createMultipleBooks(listOfBooks);
    }
    @DeleteMapping("/delete-book-by-id/{id}")
    public void deleteBookById(@PathVariable Long id){
        bookService.deleteBookById(id);
    }
    @PostMapping("/reset-library")
    public String deleteAllBooksAndResetAutoIncrement(){
        bookService.deleteAllBooksAndResetAutoIncrement();
        return "All the data in library deleted and reset";
    }



}

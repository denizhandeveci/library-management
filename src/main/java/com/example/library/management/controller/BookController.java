package com.example.library.management.controller;

import com.example.library.management.entity.BookEntity;
import com.example.library.management.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @PostMapping("/create-book")
    public BookEntity createBook(@RequestBody BookEntity bookEntity){
        return bookService.createBook(bookEntity);
    }

}

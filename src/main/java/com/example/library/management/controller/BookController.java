package com.example.library.management.controller;

import com.example.library.management.entity.BookEntity;
import com.example.library.management.service.BookService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<BookEntity> createBook(@RequestParam String title,
                                                 @RequestParam String author,
                                                 @RequestParam String isbn,
                                                 @RequestParam String genre,
                                                 @RequestParam Integer numOfCopies,
                                                 @RequestParam Boolean available,
                                                 @RequestParam MultipartFile coverImage) {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setTitle(title);
        bookEntity.setAuthor(author);
        bookEntity.setIsbn(isbn);
        bookEntity.setGenre(genre);
        bookEntity.setNumOfCopies(numOfCopies);
        bookEntity.setAvailable(available);

        BookEntity saved = bookService.createBook(bookEntity, coverImage);

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



}

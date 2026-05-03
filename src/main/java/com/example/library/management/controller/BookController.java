package com.example.library.management.controller;

import com.example.library.management.dto.BookRequestDTO;
import com.example.library.management.dto.BookResponseDTO;
import com.example.library.management.entity.BookEntity;
import com.example.library.management.repository.BookRepository;
import com.example.library.management.service.BookService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Book;
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
    public ResponseEntity<BookResponseDTO> updateBook(@PathVariable("id") Long id, @RequestBody BookRequestDTO bookRequestDTO){
        return bookService.updateBook(id, bookRequestDTO);
    }

    @PostMapping(value = "/create-book")
    public ResponseEntity<BookResponseDTO> createBook(@RequestBody BookRequestDTO bookRequestDTO)  {

        BookResponseDTO saved = bookService.create(bookRequestDTO);

        return ResponseEntity.ok(saved);
    }
    @PostMapping("/create-multiple-books")
    public List<BookResponseDTO> createMultipleBooks(@RequestBody List<BookRequestDTO> listOfBooks){
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
    public List<BookResponseDTO> getAllBooksSortedByAuthorAsc() {
        return bookService.getAllBooksSortedByAuthorAsc();
    }

    @GetMapping("/get-all-books")
    public List<BookResponseDTO> getAllBooks(){
        return bookService.getAllBooks();
    }

}

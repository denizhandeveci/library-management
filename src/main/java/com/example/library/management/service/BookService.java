package com.example.library.management.service;

import com.example.library.management.entity.BookEntity;
import com.example.library.management.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }


    public BookEntity createBook(BookEntity bookEntity){
        return bookRepository.save(bookEntity);
    }

    public void deleteBookById(Long id){
        bookRepository.deleteById(id);

    }

//    public BookEntity updateBook(Long id, BookEntity bookEntity){
//        BookEntity bookEntity1 = bookRepository.findById(id).orElseThrow();

}

package com.example.library.management.service;

import com.example.library.management.entity.BookEntity;
import com.example.library.management.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<BookEntity> createMultipleBooks(List<BookEntity> listOfBooks){
        return bookRepository.saveAll(listOfBooks);
    }

    public void deleteBookById(Long id){
        bookRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllBooksAndResetAutoIncrement() {
        bookRepository.deleteAll();
        bookRepository.resetAutoIncrement();
    }




}

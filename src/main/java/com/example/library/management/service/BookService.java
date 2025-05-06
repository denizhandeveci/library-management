package com.example.library.management.service;

import com.example.library.management.entity.BookEntity;
import com.example.library.management.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    public String searchBookByTitle(String title) {
        BookEntity bookEntity = bookRepository.findByTitle(title)
                .orElseThrow(() -> new NoSuchElementException("Book with title '" + title + "' not found."));

        return"'" + bookEntity.getTitle()+ "'" + " is in the library and has " +
                bookEntity.getNumOfCopies() + " copies available.";
    }

    public String viewBook(Long id){
        List<String> recommandationsByGenreList = new ArrayList<>();
        List<String> recommadationsByAuthorList = new ArrayList<>();
        Optional<BookEntity> bookEntity = bookRepository.findById(id);
        if(bookEntity.isEmpty()){
            return "Book with Id: " + id + " is not found";
        }

        List<BookEntity> sameGenreBooks = bookRepository.findByGenre(bookEntity.get().getGenre() );
        List<BookEntity> sameAuthorBooks = bookRepository.findByAuthor(bookEntity.get().getAuthor());

        for(BookEntity b:sameGenreBooks){
            if(!b.getId().equals(id)){
                recommandationsByGenreList.add(b.getTitle());
            }
        }
        for(BookEntity b:sameAuthorBooks){
            if(!b.getId().equals(id)){
                recommadationsByAuthorList.add(b.getTitle());
            }
        }
        return "Here is the information about the book you are searching for:" + bookEntity.toString() +
                "People who liked this genre also borrowed: " + recommandationsByGenreList +
                " People who liked this author also borrowed: " + recommadationsByAuthorList;
    }

    







}

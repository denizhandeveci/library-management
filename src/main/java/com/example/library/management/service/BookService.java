package com.example.library.management.service;

import com.example.library.management.entity.BookEntity;
import com.example.library.management.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    public BookEntity createBook(BookEntity bookEntity, MultipartFile coverImage){
        System.out.println("File empty? " + coverImage.isEmpty());
        if (coverImage != null && !coverImage.isEmpty()) {
            try {
                // Folder where images will be stored
                String uploadDirectory = "uploads/";

                // Convert folder path into Java Path object
                Path uploadPath = Paths.get(uploadDirectory);

                // Create folder if it doesn't exist
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Get original file name (for example, "book.png")
                String originalFileName = coverImage.getOriginalFilename();
                System.out.println("Original file name: " + coverImage.getOriginalFilename());

                // Extract file extension (for example, ".png")
                String fileExtension = "";
                if (originalFileName != null && originalFileName.contains(".")) {
                    fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                }

                // Generate unique file name to avoid conflicts
                String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

                // Full file path: uploads/unique-name.png
                Path filePath = uploadPath.resolve(uniqueFileName);

                // Save file to disk
                Files.copy(coverImage.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Save URL/path in database
                bookEntity.setCoverImageUrl("/uploads/" + uniqueFileName);
                System.out.println("Generated URL: " + "/uploads/" + uniqueFileName);

            } catch (IOException e) {
                // Handle file saving errors
                throw new RuntimeException("Failed to save cover image", e);
            }
        }

// Save book in database
        System.out.println("Book URL before save: " + bookEntity.getCoverImageUrl());
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
                bookEntity.getNumOfCopiesAvailable() + " copies available.";
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

    public List<BookEntity> getAllBooksSortedByAuthorAsc(){
        return bookRepository.getAllBooksSortedByAuthorAsc();
    }
}

package com.example.library.management.service;

import com.example.library.management.dto.BookRequestDTO;
import com.example.library.management.dto.BookResponseDTO;
import com.example.library.management.entity.BookEntity;
import com.example.library.management.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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

    public ResponseEntity<BookResponseDTO> updateBook(Long id,BookRequestDTO bookRequestDTO) {
        BookEntity bookEntity = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + id));
        int downgradedBookNumber = bookRequestDTO.getNumOfTotalCopies() - bookEntity.getNumOfTotalCopies();

        if (downgradedBookNumber < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The total number of copies cannot be reduced below zero.");
        }

        int pseudoNumOfAvailableCopy = downgradedBookNumber + bookEntity.getNumOfCopiesAvailable();
        bookEntity.setNumOfCopiesAvailable(pseudoNumOfAvailableCopy);
        bookEntity.setTitle(bookRequestDTO.getTitle());
        bookEntity.setAuthor(bookRequestDTO.getAuthor());
        bookEntity.setGenre(bookRequestDTO.getGenre());
        bookEntity.setIsbn(bookRequestDTO.getIsbn());
        bookEntity.setNumOfTotalCopies(bookRequestDTO.getNumOfTotalCopies());
        bookEntity.setCoverImageUrl(bookRequestDTO.getCoverImageUrl());

        bookEntity = bookRepository.save(bookEntity);

        return ResponseEntity.ok(mapToDTO(bookEntity));
    }

    public BookResponseDTO create(BookRequestDTO bookRequestDTO){
        BookEntity bookEntity = new BookEntity();
        bookEntity.setTitle(bookRequestDTO.getTitle());
        bookEntity.setAuthor(bookRequestDTO.getAuthor());
        bookEntity.setIsbn(bookRequestDTO.getIsbn());
        bookEntity.setGenre(bookRequestDTO.getGenre());
        bookEntity.setNumOfTotalCopies(bookRequestDTO.getNumOfTotalCopies());
        bookEntity.setCoverImageUrl(bookRequestDTO.getCoverImageUrl());
        bookEntity.setAvailable(true);
        bookEntity.setNumOfCopiesAvailable(bookRequestDTO.getNumOfTotalCopies());

        bookEntity = bookRepository.save(bookEntity);
        return mapToDTO(bookEntity);
    }

    public BookResponseDTO createBook(BookRequestDTO bookRequestDTO, MultipartFile coverImage){
        System.out.println("File empty? " + coverImage.isEmpty());
        BookEntity bookEntity = mapToEntity(bookRequestDTO);
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
        BookEntity savedBook = bookRepository.save(bookEntity);
        return mapToDTO(savedBook);

    }

    public List<BookResponseDTO> createMultipleBooks(List<BookRequestDTO> listOfBooks){
        List<BookEntity> books = listOfBooks.stream()
                .map(this::mapToEntity)
                .toList();
        List<BookEntity> savedBooks = bookRepository.saveAll(books);

        return savedBooks.stream()
                .map(this::mapToDTO)
                .toList();
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

        List<BookEntity> sameGenreBooks = bookRepository.fetchBooksByGenre(bookEntity.get().getGenre() );
        List<BookEntity> sameAuthorBooks = bookRepository.fetchBooksByAuthorName(bookEntity.get().getAuthor());

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

    public List<BookResponseDTO> getAllBooksSortedByAuthorAsc(){
        return bookRepository.getAllBooksSortedByAuthorAsc()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<BookResponseDTO> getAllBooks(){
        return bookRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public BookResponseDTO mapToDTO (BookEntity bookEntity){
        BookResponseDTO dto = new BookResponseDTO();

        dto.setId(bookEntity.getId());
        dto.setTitle(bookEntity.getTitle());
        dto.setAuthor(bookEntity.getAuthor());
        dto.setIsbn(bookEntity.getIsbn());
        dto.setGenre(bookEntity.getGenre());
        dto.setNumOfTotalCopies(bookEntity.getNumOfTotalCopies());
        dto.setNumOfCopiesAvailable(bookEntity.getNumOfCopiesAvailable());
        dto.setAvailable(bookEntity.isAvailable());
        dto.setCoverImageUrl(bookEntity.getCoverImageUrl());

        return dto;
    }

    private BookEntity mapToEntity(BookRequestDTO dto) {
        BookEntity bookEntity = new BookEntity();

        bookEntity.setTitle(dto.getTitle());
        bookEntity.setAuthor(dto.getAuthor());
        bookEntity.setIsbn(dto.getIsbn());
        bookEntity.setGenre(dto.getGenre());
        bookEntity.setNumOfTotalCopies(dto.getNumOfTotalCopies());
        bookEntity.setNumOfCopiesAvailable(dto.getNumOfCopiesAvailable());
        bookEntity.setAvailable(dto.getAvailable());

        return bookEntity;
    }
}

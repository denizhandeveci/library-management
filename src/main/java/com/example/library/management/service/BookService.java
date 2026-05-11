package com.example.library.management.service;

import com.example.library.management.dto.book.BookRequest;
import com.example.library.management.dto.book.BookResponse;
import com.example.library.management.dto.book.BookSortField;
import com.example.library.management.entity.Book;
import com.example.library.management.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class BookService
{

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookResponse> getBooks(String title, BookSortField sortBy, Sort.Direction direction) {
        Sort sort = Sort.by(direction, sortBy.propertyName());

        List<Book> books;

        if (title != null && !title.isBlank()) {
            books = bookRepository.searchByTitle(title, sort);
        } else {
            books = bookRepository.findAllSorted(sort);
        }

        return books.stream()
                .map(BookResponse::fromEntity)
                .toList();
    }

    public BookResponse createBook(BookRequest bookRequest) {
        Book entity = bookRequest.toEntity();

        entity = bookRepository.save(entity);
        return BookResponse.fromEntity(entity);
    }

    // TODO clarify: we probably need to use this method instead and allow the admins to                            -  yb
    //      upload an image for the book. Rn we ask for the cover image url, which isn't realistic.
    // TODO further cleanup: creating the folder if it doesn't exist probably shouldn't be part of this method's    - yb
    //      responsibility. Alternative approach (better?):
    //            -> read the upload path from an env variable. Crash backend-startup if it's not specified
    //            -> or it's an invalid folder that doesn't exist
    public BookResponse createBook(BookRequest bookRequest, MultipartFile coverImage) {
        var isCoverImageEmpty = coverImage == null || coverImage.isEmpty();
        System.out.println("File empty? " + isCoverImageEmpty);

        Book book = bookRequest.toEntity();
        var coverImageUrl = "";

        if (!isCoverImageEmpty) {
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
                coverImageUrl = "/uploads/" + uniqueFileName;
                System.out.println("Generated URL: " + "/uploads/" + uniqueFileName);

            } catch (IOException e) {
                // Handle file saving errors
                throw new RuntimeException("Failed to save cover image", e);
            }
        }

        book.coverImageUrl = coverImageUrl;

        // Save book in database
        System.out.println("Book URL before save: " + book.coverImageUrl);
        Book savedBook = bookRepository.save(book);
        return BookResponse.fromEntity(savedBook);

    }

    public ResponseEntity<BookResponse> updateBook(Long id, BookRequest bookRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + id));

        int loanedCopies = book.numOfTotalCopies - book.numOfCopiesAvailable;
        int newTotalCopies = bookRequest.numOfTotalCopies();

        if (newTotalCopies < loanedCopies) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The total number of copies cannot be lower than the number of currently loaned copies."
            );
        }

        book.title = bookRequest.title();
        book.author = bookRequest.author();
        book.genre = bookRequest.genre();
        book.isbn = bookRequest.isbn();

        book.numOfTotalCopies = newTotalCopies;
        book.numOfCopiesAvailable = newTotalCopies - loanedCopies;
        book.coverImageUrl = bookRequest.coverImageUrl();

        book = bookRepository.save(book);

        return ResponseEntity.ok(BookResponse.fromEntity(book));
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllBooksAndResetAutoIncrement() {
        bookRepository.deleteAll();
        bookRepository.resetAutoIncrement();
    }

    public String searchBookByTitle(String title) {
        Book bookEntity = bookRepository.findByTitle(title)
                .orElseThrow(() -> new NoSuchElementException("Book with title '" + title + "' not found."));

        // TODO discuss: code smell? The exact response layout isn't the job of the backend, backend should rather return a structured, yb
        //  response JSON. FE can then display it however it likes.
        return "'" + bookEntity.title + "'" + " is in the library and has " +
                bookEntity.numOfCopiesAvailable + " copies available.";
    }

    public String viewBook(Long id) {
        List<String> recommendationsByGenre = new ArrayList<>();
        List<String> recommendationsByAuthor = new ArrayList<>();

        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            return "Book with Id: " + id + " is not found";
        }

        List<Book> sameGenreBooks = bookRepository.findByGenre(book.genre);
        List<Book> sameAuthorBooks = bookRepository.findByAuthor(book.author);

        for (Book sameGenreBook : sameGenreBooks) {
            var isSameBook = book.id.equals(sameGenreBook.id);
            if (!isSameBook) {
                recommendationsByGenre.add(sameGenreBook.title);
            }
        }
        for (Book sameAuthorBook : sameAuthorBooks) {
            var isSameBook = book.id.equals(sameAuthorBook.id);
            if (!isSameBook) {
                recommendationsByAuthor.add(sameAuthorBook.title);
            }
        }

        // TODO same here, backend should return a structured response, frontend should display it however it likes, yb
        return "Here is the information about the book you are searching for:" + book +
                "People who liked this genre also borrowed: " + recommendationsByGenre +
                " People who liked this author also borrowed: " + recommendationsByAuthor;
    }
}

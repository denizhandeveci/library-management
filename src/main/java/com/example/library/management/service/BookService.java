package com.example.library.management.service;

import com.example.library.management.dto.book.BookDetailsResponse;
import com.example.library.management.dto.book.BookDetailsResponse.BookRecommendation;
import com.example.library.management.dto.book.BookRequest;
import com.example.library.management.dto.book.BookResponse;
import com.example.library.management.dto.book.BookSortField;
import com.example.library.management.entity.Book;
import com.example.library.management.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class BookService
{

    private final BookRepository bookRepository;

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

    public BookDetailsResponse getBookDetails(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + bookId));

        List<BookRecommendation> recommendationsByGenre = bookRepository.findByGenre(book.genre)
                .stream()
                .filter(sameGenreBook -> !book.id.equals(bookId)) // filter out book-at-hand
                .map(BookRecommendation::fromEntity)
                .toList();

        List<BookRecommendation> recommendationsByAuthor = bookRepository.findByAuthor(book.author)
                .stream()
                .filter(sameAuthorBook -> !book.id.equals(bookId))
                .map(BookRecommendation::fromEntity)
                .toList();

        return new BookDetailsResponse(
                BookResponse.fromEntity(book),
                recommendationsByGenre,
                recommendationsByAuthor
        );
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
                String uniqueFileName = UUID.randomUUID() + fileExtension;

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

    @Transactional
    public BookResponse updateBook(Long id, BookRequest bookRequest) {
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

        return BookResponse.fromEntity(book);
    }

    @Transactional
    public void softDeleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));

        book.softDelete();
    }

    @Transactional
    public void deleteAllBooksAndResetAutoIncrement() {
        bookRepository.deleteAll();
        bookRepository.resetAutoIncrement();
    }
}

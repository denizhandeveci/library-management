package com.example.library.management.service;

import com.example.library.management.dto.book.BookDetailsResponse;
import com.example.library.management.dto.book.BookDetailsResponse.BookRecommendation;
import com.example.library.management.dto.book.BookRequest;
import com.example.library.management.dto.book.BookResponse;
import com.example.library.management.dto.book.BookSortField;
import com.example.library.management.entity.Book;
import com.example.library.management.exception.BadRequestException;
import com.example.library.management.exception.ResourceNotFoundException;
import com.example.library.management.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    private static final Logger log = LoggerFactory.getLogger(BookService.class);

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookResponse> getBooks(String title, BookSortField sortBy, Sort.Direction direction) {
        Sort sort = Sort.by(direction, sortBy.propertyName());

        List<Book> books;

        if (title != null && !title.isBlank()) {
            books = bookRepository.searchByTitle(title, sort);
            log.debug("Found {} books matching title={} sorted by {} {}", books.size(), title, sortBy, direction);

        } else {
            books = bookRepository.findAllSorted(sort);
            log.debug("Found {} books sorted by {} {}", books.size(), sortBy, direction);
        }

        return books.stream()
                .map(BookResponse::fromEntity)
                .toList();
    }

    public BookDetailsResponse getBookDetails(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Book", bookId));

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


        log.debug(
                "Loaded book details for bookId={} with {} genre recommendations and {} author recommendations",
                bookId,
                recommendationsByGenre.size(),
                recommendationsByAuthor.size()
        );

        return new BookDetailsResponse(
                BookResponse.fromEntity(book),
                recommendationsByGenre,
                recommendationsByAuthor
        );
    }

    public BookResponse createBook(BookRequest bookRequest) {
        log.info("Creating book with title={} and isbn={}", bookRequest.title(), bookRequest.isbn());

        Book entity = bookRequest.toEntity();

        entity = bookRepository.save(entity);

        log.info("Book created successfully without upload with bookId={} and isbn={}", entity.id, entity.isbn);

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

        log.info(
                "Creating book with title={}, isbn={}, hasCoverImage={}",
                bookRequest.title(),
                bookRequest.isbn(),
                !isCoverImageEmpty
        );

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
                    log.info("Created upload directory at path={}", uploadPath.toAbsolutePath());
                }

                // Get original file name (for example, "book.png")
                String originalFileName = coverImage.getOriginalFilename();
                log.debug("Received cover image file name={} for isbn={}", originalFileName, bookRequest.isbn());

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

                log.info("Saved cover image for isbn={} at url={}", bookRequest.isbn(), coverImageUrl);

            } catch (IOException e) {
                log.warn("Failed to save cover image for isbn={}", bookRequest.isbn());

                throw new RuntimeException("Failed to save cover image", e);
            }
        }

        book.coverImageUrl = coverImageUrl;

        Book savedBook = bookRepository.save(book);

        log.info("Book created successfully with cover upload with bookId={} and isbn={}", savedBook.id, savedBook.isbn);

        return BookResponse.fromEntity(savedBook);

    }

    @Transactional
    public BookResponse updateBook(Long id, BookRequest bookRequest) {
        log.info("Updating book with bookId={}", id);

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Book", id));

        int loanedCopies = book.numOfTotalCopies - book.numOfCopiesAvailable;
        int newTotalCopies = bookRequest.numOfTotalCopies();

        if (newTotalCopies < loanedCopies) {
            throw new BadRequestException("The total number of copies cannot be lower than the number of currently loaned copies.");
        }

        book.title = bookRequest.title();
        book.author = bookRequest.author();
        book.genre = bookRequest.genre();
        book.isbn = bookRequest.isbn();

        book.numOfTotalCopies = newTotalCopies;
        book.numOfCopiesAvailable = newTotalCopies - loanedCopies;
        book.coverImageUrl = bookRequest.coverImageUrl();

        book = bookRepository.save(book);


        log.info(
                "Book updated successfully with bookId={}, totalCopies={}, availableCopies={}",
                book.id,
                book.numOfTotalCopies,
                book.numOfCopiesAvailable
        );

        return BookResponse.fromEntity(book);
    }

    @Transactional
    public void softDeleteBook(Long id) {
        log.info("Soft deleting book with bookId={}", id);

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Book", id));

        book.softDelete();
        log.info("Book soft deleted successfully with bookId={}", id);
    }

    @Transactional
    public void deleteAllBooksAndResetAutoIncrement() {
        log.warn("Deleting all books and resetting auto increment");

        bookRepository.deleteAll();
        bookRepository.resetAutoIncrement();

        log.warn("All books deleted and auto increment reset");
    }
}

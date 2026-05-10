package com.example.library.management.repository;

import com.example.library.management.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    @Modifying
    @Query(value = "ALTER TABLE books AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
    Optional<Book> findByTitle(String title);

    @Query("SELECT b FROM Book b WHERE b.genre = :genre")
    List<Book> fetchBooksByGenre(@Param("genre") String genre);

    @Query("SELECT b FROM Book b WHERE b.author = :authorName")
    List<Book> fetchBooksByAuthorName(@Param("authorName") String authorName);

    //List<BookEntity> findByAuthor(String author);

    @Query("SELECT b FROM Book b ORDER BY b.author ASC")
    List<Book> getAllBooksSortedByAuthorAsc();

    @Query("SELECT b FROM Book b ORDER BY b.author DESC")
    List<Book> getAllBooksSortedByAuthorDesc();
}

package com.example.library.management.repository;

import com.example.library.management.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity,Long> {
    @Modifying
    @Query(value = "ALTER TABLE book_entity AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
    Optional<BookEntity> findByTitle(String title);

    @Query("SELECT b FROM BookEntity b WHERE b.genre = :genre")
    List<BookEntity> fetchBooksByGenre(@Param("genre") String genre);

    @Query("SELECT b FROM BookEntity b WHERE b.author = :authorName")
    List<BookEntity> fetchBooksByAuthorName(@Param("authorName") String authorName);

    //List<BookEntity> findByAuthor(String author);

    @Query("SELECT b FROM BookEntity b ORDER BY b.author ASC")
    List<BookEntity> getAllBooksSortedByAuthorAsc();

    @Query("SELECT b FROM BookEntity b ORDER BY b.author DESC")
    List<BookEntity> getAllBooksSortedByAuthorDesc();
}

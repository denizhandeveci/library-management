package com.example.library.management.repository;

import com.example.library.management.entity.Book;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>
{
    @Modifying
    @Query(value = "ALTER TABLE books AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();

    @Query("""             
            SELECT b
            FROM Book b
            WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%') )
                AND b.deleted is null
            """
    )
    List<Book> searchByTitle(@Param("title") String title, Sort sort);

    @Query("SELECT b FROM Book b WHERE b.genre = :genre AND b.deleted is NULL")
    List<Book> findByGenre(@Param("genre") String genre);

    @Query("SELECT b FROM Book b WHERE b.author = :author AND b.deleted is NULL")
    List<Book> findByAuthor(@Param("author") String author);

    @Query("SELECT b FROM Book b WHERE b.deleted is NULL")
    List<Book> findAllSorted(Sort sort);
}

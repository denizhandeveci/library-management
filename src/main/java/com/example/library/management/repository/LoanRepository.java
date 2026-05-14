package com.example.library.management.repository;

import com.example.library.management.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long>
{
    @Query("""
            SELECT l
            FROM Loan l
            WHERE l.id = :id
                AND l.returnDate is NULL
                AND l.deleted is NULL
            """)
    Optional<Loan> findOpenLoanById(@Param("id") Long id);

    @Query("""
            SELECT l
            FROM Loan l
            WHERE l.book = :bookId
                AND l.user = :userId
                AND l.returnDate is NULL
                AND l.deleted is NULL
            """)
    Optional<Loan> findOpenLoanByUserIdAndBookId(@Param("userId") Long userId, @Param("bookId") Long bookId);

    @Query("""
            SELECT COUNT(l) > 0
            FROM Loan l
            WHERE l.book.id = :bookId
                AND l.user.id = :userId
                AND l.returnDate is NULL
                AND l.deleted is NULL
            """)
    boolean existsOpenLoanByUserIdAndBookId(@Param("userId") Long userId, @Param("bookId") Long bookId);

    @Query("""
            SELECT l
            FROM Loan l
            WHERE l.user.id = :userId
                AND l.returnDate is NULL
                AND l.deleted is NULL
            """)
    List<Loan> findOpenLoansByUserId(@Param("userId") Long userId);

}

package com.example.library.management.repository;

import com.example.library.management.entity.Reservation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>
{
    @Modifying
    @Query(value = "ALTER TABLE reservations AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();

    @Query("""
            SELECT r
            FROM Reservation r
            WHERE r.book.id = :bookId
                AND r.deleted is NULL
            ORDER BY r.reservationDate ASC, r.id ASC
            """)
    List<Reservation> findActiveReservationsByBookId(
            @Param("bookId") Long bookId,
            Pageable pageable
    );

    @Query("""
            SELECT COUNT(r) > 0
            FROM Reservation r
            WHERE r.book.id = :bookId
                AND r.user.id = :userId
                AND r.deleted is NULL
            """)
    boolean existsByBookIdAndUserId(
            @Param("bookId") Long bookId,
            @Param("userId") Long userId
    );

    @Query("""
            SELECT r
            FROM Reservation r
            WHERE r.user.id = :userId
                AND r.deleted is NULL
            """)
    List<Reservation> findAllByUserId(@Param("userId") Long userId);
}

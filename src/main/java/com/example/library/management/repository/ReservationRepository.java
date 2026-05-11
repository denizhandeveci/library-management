package com.example.library.management.repository;

import com.example.library.management.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    Optional<Reservation> findFirstByBookIdOrderByIdAsc(Long bookId);

    //checks if a reservation exists by the same book and same user
    boolean existsByBookIdAndUserId(Long bookId, Long userId);

    List<Reservation> findAllByUserId(Long userId);
}

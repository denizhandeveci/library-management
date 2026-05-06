package com.example.library.management.repository;

import com.example.library.management.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity,Long> {

    Optional<ReservationEntity> findFirstByBookEntityIdOrderByIdAsc(Long bookId);
    //checks if a reservation exists by the same book and same user
    boolean existsByBookEntityIdAndUserEntityId(Long bookId, Long userId);
    List<ReservationEntity> findAllByUserEntityId(Long userId);
}

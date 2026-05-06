package com.example.library.management.repository;

import com.example.library.management.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity,Long> {

    // This query simply finds loan by user and book which is currently not returned and still and active loan
    Optional<LoanEntity> findByUserEntityIdAndBookEntityIdAndIsReturnedFalse(Long userId, Long bookId);


    boolean existsByBookEntityIdAndUserEntityIdAndIsReturnedFalse(Long bookId, Long userId);

    List<LoanEntity> findAllByUserEntityIdAndIsReturnedFalse(Long userId);

}

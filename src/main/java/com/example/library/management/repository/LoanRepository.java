package com.example.library.management.repository;

import com.example.library.management.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity,Long> {

    @Query(" SELECT l FROM LoanEntity WHERE l.bookEntity.id = :bookId AND l.userEntity.id = :userId ")
    LoanEntity  getLoanEntityByBookEntityIdAndUserEntityId (@Param("userId") Long userId, @Param("bookId") Long bookId);

}

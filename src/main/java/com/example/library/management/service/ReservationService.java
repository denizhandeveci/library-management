package com.example.library.management.service;

import com.example.library.management.entity.BookEntity;
import com.example.library.management.entity.ReservationEntity;
import com.example.library.management.entity.UserEntity;
import com.example.library.management.repository.BookRepository;
import com.example.library.management.repository.ReservationRepository;
import com.example.library.management.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;
    private BookRepository bookRepository;
    private UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              BookRepository bookRepository,
                              UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public ReservationEntity makeReservation(Long userId, Long bookId){
        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setReservationDate(LocalDate.now());
        reservationEntity.setUserId(userRepository.findById(userId).orElseThrow());
        reservationEntity.setBookId(bookRepository.findById(bookId).orElseThrow());
        return reservationRepository.save(reservationEntity);
    }
}

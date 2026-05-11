package com.example.library.management.service;

import com.example.library.management.dto.ReservationResponse;
import com.example.library.management.entity.Book;
import com.example.library.management.entity.Reservation;
import com.example.library.management.entity.UserEntity;
import com.example.library.management.repository.BookRepository;
import com.example.library.management.repository.ReservationRepository;
import com.example.library.management.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService
{

    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              BookRepository bookRepository,
                              UserRepository userRepository)
    {
        this.reservationRepository = reservationRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public List<ReservationResponse> getAllReservations(Long userId) {
        return reservationRepository.findAllByUserId(userId)
                .stream()
                .map(ReservationResponse::fromEntity)
                .toList();
    }

    public ReservationResponse makeReservation(Long userId, Long bookId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        boolean reservationExists = reservationRepository.existsByBookIdAndUserId(book.id, user.getId());
        if (reservationExists) {
            throw new IllegalStateException("User ID with" + userId + "already has a reservation for this book.");
        }

        Reservation savedReservation = reservationRepository.save(createReservationEntity(user, book));

        return ReservationResponse.fromEntity(savedReservation);
    }

    public void deleteReservationById(Long reservationId) {
        reservationRepository.deleteById(reservationId);

    }

    public Reservation createReservationEntity(UserEntity user, Book book) {
        Reservation reservationEntity = new Reservation();

        reservationEntity.user = user;
        reservationEntity.book = book;

        reservationEntity.reservationDate = LocalDate.now();

        return reservationEntity;
    }


}

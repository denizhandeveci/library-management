package com.example.library.management.service;

import com.example.library.management.dto.ReservationResponse;
import com.example.library.management.entity.Book;
import com.example.library.management.entity.Reservation;
import com.example.library.management.entity.User;
import com.example.library.management.exception.ConflictException;
import com.example.library.management.exception.ResourceNotFoundException;
import com.example.library.management.repository.BookRepository;
import com.example.library.management.repository.ReservationRepository;
import com.example.library.management.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService
{
    private static final Logger log = LoggerFactory.getLogger(ReservationService.class);

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
        List<ReservationResponse> reservations = reservationRepository.findAllByUserId(userId)
                .stream()
                .map(ReservationResponse::fromEntity)
                .toList();

        log.debug("Found {} reservations for userId={}", reservations.size(), userId);

        return reservations;
    }

    public ReservationResponse makeReservation(Long userId, Long bookId) {
        log.info("Creating reservation for userId={} and bookId={}", userId, bookId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> ResourceNotFoundException.forId("User", userId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Book", bookId));

        boolean reservationExists = reservationRepository.existsByBookIdAndUserId(book.id, user.id);
        if (reservationExists) {
            throw new ConflictException("User ID with" + userId + "already has a reservation for bookId=" + bookId + ".");
        }

        Reservation savedReservation = reservationRepository.save(createReservationEntity(user, book));

        log.info("Reservation created successfully with reservationId={} for userId={} and bookId={}", savedReservation.id, userId, bookId);

        return ReservationResponse.fromEntity(savedReservation);
    }

    public void deleteReservationById(Long reservationId) {
        log.info("Deleting reservation with reservationId={}", reservationId);

        if (!reservationRepository.existsById(reservationId)) {
            throw ResourceNotFoundException.forId("Reservation", reservationId);
        }

        reservationRepository.deleteById(reservationId);

        log.info("Reservation deleted successfully with reservationId={}", reservationId);
    }

    public Reservation createReservationEntity(User user, Book book) {
        Reservation reservationEntity = new Reservation();

        reservationEntity.user = user;
        reservationEntity.book = book;

        reservationEntity.reservationDate = LocalDate.now();

        return reservationEntity;
    }
}

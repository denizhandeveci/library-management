package com.example.library.management.service;

import com.example.library.management.dto.ReservationResponseDTO;
import com.example.library.management.entity.Book;
import com.example.library.management.entity.ReservationEntity;
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

    public List<ReservationResponseDTO> getAllReservations(Long userId) {
        return reservationRepository.findAllByUserEntityId(userId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public ReservationResponseDTO makeReservation(Long userId, Long bookId) {

        boolean exists = reservationRepository.existsByBookEntityIdAndUserEntityId(bookId, userId);

        if (exists) {
            throw new IllegalStateException("User ID with" + userId + "already has a reservation for this book.");
        }

        ReservationEntity reservationEntity = mapToEntity(userId, bookId);
        ReservationEntity savedReservation = reservationRepository.save(reservationEntity);

        return mapToDTO(savedReservation);
    }

    public void deleteReservationById(Long reservationId) {
        reservationRepository.deleteById(reservationId);

    }

    public ReservationResponseDTO mapToDTO(ReservationEntity reservationEntity) {

        ReservationResponseDTO reservationResponseDTO = new ReservationResponseDTO();

        reservationResponseDTO.setId(reservationEntity.getId());
        reservationResponseDTO.setUserId(reservationEntity.getUserEntity().getId());
        reservationResponseDTO.setUserName(reservationEntity.getUserEntity().getName());
        reservationResponseDTO.setBookId(reservationEntity.getBookEntity().id);
        reservationResponseDTO.setBookTitle(reservationEntity.getBookEntity().title);
        reservationResponseDTO.setReservationDate(reservationEntity.getReservationDate());
        reservationResponseDTO.setIsbn(reservationEntity.getBookEntity().isbn);
        reservationResponseDTO.setAuthor(reservationEntity.getBookEntity().author);
        reservationResponseDTO.setGenre(reservationEntity.getBookEntity().genre);

        return reservationResponseDTO;

    }


    public ReservationEntity mapToEntity(Long userId, Long bookId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        ReservationEntity reservationEntity = new ReservationEntity();

        reservationEntity.setUserId(user);
        reservationEntity.setBookId(book);
        reservationEntity.setReservationDate(LocalDate.now());

        return reservationEntity;
    }


}

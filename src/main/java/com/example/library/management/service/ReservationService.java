package com.example.library.management.service;

import com.example.library.management.dto.ReservationRequestDTO;
import com.example.library.management.dto.ReservationResponseDTO;
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

    public ReservationResponseDTO makeReservation(ReservationRequestDTO reservationRequestDTO){

        boolean exists = reservationRepository
                .existsByBookEntityIdAndUserEntityId(reservationRequestDTO.getBookId(), reservationRequestDTO.getUserId());

        if (exists) {
            throw new IllegalStateException
                    ("User ID with" + reservationRequestDTO.getUserId() +
                    "already has a reservation for this book.");
        }
        ReservationEntity reservationEntity = mapToEntity(reservationRequestDTO);
        ReservationEntity savedReservation = reservationRepository.save(reservationEntity);

        return mapToDTO(reservationEntity);
    }

    public void deleteReservationById(Long reservationId){
        reservationRepository.deleteById(reservationId);

    }
    public ReservationResponseDTO mapToDTO(ReservationEntity reservationEntity){

        ReservationResponseDTO reservationResponseDTO = new ReservationResponseDTO();

        reservationResponseDTO.setId(reservationEntity.getId());
        reservationResponseDTO.setUserId(reservationEntity.getUserEntity().getId());
        reservationResponseDTO.setUserName(reservationEntity.getUserEntity().getName());
        reservationResponseDTO.setBookId(reservationEntity.getBookEntity().getId());
        reservationResponseDTO.setBookTitle(reservationEntity.getBookEntity().getTitle());
        reservationResponseDTO.setReservationDate(reservationEntity.getReservationDate());

        return reservationResponseDTO;

    }


    public ReservationEntity mapToEntity(ReservationRequestDTO reservationRequestDTO){

        UserEntity user = userRepository.findById(reservationRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        BookEntity book = bookRepository.findById(reservationRequestDTO.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        ReservationEntity reservationEntity = new ReservationEntity();

        reservationEntity.setUserId(user);
        reservationEntity.setBookId(book);
        reservationEntity.setReservationDate(LocalDate.now());

        return reservationEntity;
    }


}

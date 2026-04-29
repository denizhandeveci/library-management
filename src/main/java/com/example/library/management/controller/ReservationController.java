package com.example.library.management.controller;

import com.example.library.management.dto.ReservationRequestDTO;
import com.example.library.management.dto.ReservationResponseDTO;
import com.example.library.management.entity.ReservationEntity;
import com.example.library.management.service.ReservationService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationController {

    private ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/make-reservation/{userId}/{bookId}")
    public ReservationResponseDTO makeReservation(@RequestBody ReservationRequestDTO reservationRequestDTO){
        return reservationService.makeReservation(reservationRequestDTO);
    }
}

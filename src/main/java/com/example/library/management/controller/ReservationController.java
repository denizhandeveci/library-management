package com.example.library.management.controller;

import com.example.library.management.dto.ReservationResponse;
import com.example.library.management.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReservationController {

    private ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/make-reservation/{userId}/{bookId}")
    public ReservationResponse makeReservation(@PathVariable Long userId, @PathVariable Long bookId) {
        return reservationService.makeReservation(userId, bookId);
    }

    @GetMapping("/get-reservations/{userId}")
    public List<ReservationResponse> getAllReservations(@PathVariable Long userId) {
        return reservationService.getAllReservations(userId);
    }
}

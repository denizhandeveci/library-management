package com.example.library.management.controller;

import com.example.library.management.dto.ReservationResponse;
import com.example.library.management.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReservationController
{

    private static final Logger log = LoggerFactory.getLogger(ReservationController.class);

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/make-reservation/{userId}/{bookId}")
    public ReservationResponse makeReservation(@PathVariable Long userId, @PathVariable Long bookId) {
        log.debug("Received reservation request for userId={} and bookId={}", userId, bookId);

        return reservationService.makeReservation(userId, bookId);
    }

    @GetMapping("/get-reservations/{userId}")
    public List<ReservationResponse> getAllReservations(@PathVariable Long userId) {
        log.debug("Received get reservations request for userId={}", userId);

        return reservationService.getAllReservations(userId);
    }
}

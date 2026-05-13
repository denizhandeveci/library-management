package com.example.library.management.controller;

import com.example.library.management.dto.LoanRequest;
import com.example.library.management.dto.ReservationRequest;
import com.example.library.management.dto.ReservationResponse;
import com.example.library.management.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReservationController {

    private static final Logger log = LoggerFactory.getLogger(ReservationController.class);

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ReservationResponse makeReservation(@RequestBody ReservationRequest reservationRequest) {
        log.debug("Received reservation request for userId={} and bookId={}", reservationRequest.userId(), reservationRequest.bookId());
        return reservationService.makeReservation(reservationRequest.userId(),
                                                  reservationRequest.bookId());
    }

    @GetMapping("/reservations/{userId}")
    public List<ReservationResponse> getAllReservations(@PathVariable Long userId) {
        log.debug("Received get reservations request for userId={}", userId);

        return reservationService.getAllReservations(userId);
    }
}

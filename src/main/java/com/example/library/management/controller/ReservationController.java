package com.example.library.management.controller;

import com.example.library.management.dto.ReservationRequest;
import com.example.library.management.dto.ReservationResponse;
import com.example.library.management.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping("/reservations/{userId}")
    public List<ReservationResponse> getAllReservations(@PathVariable Long userId) {
        return reservationService.getAllReservations(userId);
    }

    @PostMapping("/reservations")
    public ReservationResponse makeReservation(@RequestBody ReservationRequest reservationRequest) {
        log.debug("Received reservation request for userId={} and bookId={}", reservationRequest.userId(), reservationRequest.bookId());

        return reservationService.makeReservation(reservationRequest.userId(), reservationRequest.bookId());
    }

    @DeleteMapping("/reservations/{reservationId}")
    public ResponseEntity<Void> deleteReservationById(@PathVariable Long reservationId) {
        reservationService.deleteReservationById(reservationId);

        return ResponseEntity.noContent().build();
    }
}

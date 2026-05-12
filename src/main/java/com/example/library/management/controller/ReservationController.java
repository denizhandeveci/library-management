package com.example.library.management.controller;

import com.example.library.management.dto.LoanRequest;
import com.example.library.management.dto.ReservationRequest;
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

    @PostMapping("/reservations")
    public ReservationResponse makeReservation(@RequestBody ReservationRequest reservationRequest) {
        return reservationService.makeReservation(reservationRequest.userId(),
                                                  reservationRequest.bookId());
    }

    @GetMapping("/reservations/{userId}")
    public List<ReservationResponse> getAllReservations(@PathVariable Long userId) {
        return reservationService.getAllReservations(userId);
    }
}

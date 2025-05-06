package com.example.library.management.controller;

import com.example.library.management.entity.ReservationEntity;
import com.example.library.management.service.ReservationService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationController {

    private ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/make-reservation/{userId}/{bookId}")
    public ReservationEntity makeReservation(@PathVariable Long userId,@PathVariable Long bookId){
        return reservationService.makeReservation(userId,bookId);
    }
}

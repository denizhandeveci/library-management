package com.example.library.management.dto;

public record ReservationRequest(
        Long userId,
        Long bookId
) {}

package com.example.library.management.dto;

import com.example.library.management.entity.Reservation;

import java.time.LocalDate;

public record ReservationResponse(
        Long id,
        Long bookId,

        String bookTitle,
        String author,
        String genre,
        String isbn,

        Long userId,
        String userName,
        LocalDate reservationDate
)
{
    public static ReservationResponse fromEntity(Reservation reservation) {
        var book = reservation.book;
        var user = reservation.user;

        return new ReservationResponse(
                reservation.id,
                book.id,
                book.title,
                book.author,
                book.genre,
                book.isbn,

                user.id,
                user.name,

                reservation.reservationDate
        );
    }
}
package com.example.library.management.dto;

import com.example.library.management.entity.Reservation;

import java.time.LocalDate;

public record ReservationResponse(
        Long id,
        LocalDate reservationDate,

        ReservationBookDto book,
        ReservationUserDto user
)
{
    public record ReservationBookDto(
            Long bookId,
            String title,
            String author,
            String genre,
            String isbn
    ) {}

    public record ReservationUserDto(
            Long userId,
            String userName
    ) {}

    public static ReservationResponse fromEntity(Reservation reservation) {
        var book = reservation.book;
        var user = reservation.user;

        return new ReservationResponse(
                reservation.id,
                reservation.reservationDate,

                new ReservationBookDto(
                        book.id,
                        book.title,
                        book.author,
                        book.genre,
                        book.isbn
                ),

                new ReservationUserDto(
                        user.id,
                        user.name
                )
        );
    }
}
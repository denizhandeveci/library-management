package com.example.library.management.dto;

public class ReservationRequestDTO {

    private Long userId;
    private Long BookId;

    public ReservationRequestDTO(){

    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookId() {
        return BookId;
    }

    public void setBookId(Long bookId) {
        BookId = bookId;
    }
}

package com.example.library.management.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "reservation_entity")
public class ReservationEntity {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "reservation_date")
    private LocalDate reservationDate;

    @ManyToOne
    @JoinColumn(name = "user_entity_id")
    private UserEntity userEntity;
    @ManyToOne
    @JoinColumn(name = "book_entity_id")
    private BookEntity bookEntity;

    public ReservationEntity(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public UserEntity getUserId() {
        return userEntity;
    }

    public void setUserId(UserEntity userId) {
        this.userEntity = userId;
    }

    public BookEntity getBookId() {
        return bookEntity;
    }

    public void setBookId(BookEntity bookId) {
        this.bookEntity = bookId;
    }
}

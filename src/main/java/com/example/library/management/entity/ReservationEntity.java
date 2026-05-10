package com.example.library.management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "reservation_entity")
public class ReservationEntity
{
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
    private Book bookEntity;

    public ReservationEntity() {

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

    public Book getBookId() {
        return bookEntity;
    }

    public void setBookId(Book bookId) {
        this.bookEntity = bookId;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public Book getBookEntity() {
        return bookEntity;
    }

    public void setBookEntity(Book bookEntity) {
        this.bookEntity = bookEntity;
    }
}

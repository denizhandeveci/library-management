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
@Table(name = "reservations")
public class Reservation
{
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "reservation_date")
    public LocalDate reservationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public UserEntity user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    public Book book;

    public Reservation() {}
}

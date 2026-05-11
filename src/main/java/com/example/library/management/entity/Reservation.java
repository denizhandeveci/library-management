package com.example.library.management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "reservations")
public class Reservation extends BaseEntity
{
    @Override
    public String getIdPrefix() {
        return "RSV-";
    }

    @Column(name = "reservation_date", nullable = false)
    public LocalDate reservationDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    public User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    public Book book;

    public Reservation() {}
}

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
@Table(name = "loans")
public class Loan
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    public Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public UserEntity user;

    @Column(name = "loan_date")
    public LocalDate loanDate;

    @Column(name = "due_date")
    public LocalDate dueDate;

    @Column(name = "return_date")
    public LocalDate returnDate;

    @Column(name = "is_returned")
    public boolean isReturned = false;


    public Loan() {}
}

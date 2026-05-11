package com.example.library.management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "loans")
public class Loan extends BaseEntity
{
    @Override
    public String getIdPrefix() {
        return "LON-";
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    public Book book;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    public User user;

    @Column(name = "loan_date", nullable = false)
    public LocalDate loanDate;

    @Column(name = "due_date", nullable = false)
    public LocalDate dueDate;

    @Column(name = "return_date")
    public LocalDate returnDate;

    public boolean isReturned() {
        return returnDate != null;
    }

    public Loan() {}
}

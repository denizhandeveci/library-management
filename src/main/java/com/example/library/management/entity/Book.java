package com.example.library.management.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "book")
public class Book
{
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column
    public String title;

    @Column
    public Integer numOfTotalCopies;

    @Column
    public Integer numOfCopiesAvailable;

    @Column
    public String author;

    @Column
    public String isbn;

    @Column
    public String genre;

    @Column
    public Boolean available;

    @Column
    public String coverImageUrl;

    @OneToMany(mappedBy = "bookEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<LoanEntity> loans;
    @OneToMany(mappedBy = "bookEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<ReservationEntity> reservations;

    // Required for JPA
    public Book() {}

    @Override
    public String toString() {
        return "BookEntity{" +
                "title='" + title + '\'' +
                ", numOfTotalCopies=" + numOfTotalCopies +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", genre='" + genre + '\'' +
                ", available=" + available +
                '}';
    }
}

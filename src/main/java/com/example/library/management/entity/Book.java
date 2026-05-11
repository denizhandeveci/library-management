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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "title")
    public String title;

    @Column(name = "num_of_total_copies")
    public Integer numOfTotalCopies;

    @Column(name = "num_of_copies_available")
    public Integer numOfCopiesAvailable;

    @Column(name = "author")
    public String author;

    @Column(name = "isbn", unique = true)
    public String isbn;

    @Column(name = "genre")
    public String genre;

    @Column(name = "available")
    public Boolean available;

    @Column(name = "cover_image_url")
    public String coverImageUrl;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Loan> loans;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Reservation> reservations;

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

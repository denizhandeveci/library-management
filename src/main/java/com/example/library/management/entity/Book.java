package com.example.library.management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "books")
public class Book extends BaseEntity
{
    @Override
    public String getIdPrefix() {
        return "BOK-";
    }

    @Column(name = "title", nullable = false)
    public String title;

    @Column(name = "num_of_total_copies", nullable = false)
    public Integer numOfTotalCopies;

    @Column(name = "num_of_copies_available", nullable = false)
    public Integer numOfCopiesAvailable;

    @Column(name = "author", nullable = false)
    public String author;

    @Column(name = "isbn", nullable = false, unique = true)
    public String isbn;

    @Column(name = "genre")
    public String genre;

    @Column(name = "cover_image_url")
    public String coverImageUrl;

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
                ", available=" + isAvailable() +
                '}';
    }

    public boolean isAvailable() {
        return numOfCopiesAvailable > 0;
    }
}

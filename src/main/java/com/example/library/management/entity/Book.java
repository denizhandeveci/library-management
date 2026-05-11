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

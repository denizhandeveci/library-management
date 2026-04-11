package com.example.library.management.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="book_entity")
public class BookEntity {
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    private Integer numOfTotalCopies;
    private Integer numOfCopiesAvailable;
    @Column
    private String author;
    @Column
    private String isbn;
    @Column
    private String genre;
    @Column
    private Boolean available;

    private String coverImageUrl;

    @OneToMany(mappedBy = "bookEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LoanEntity> loans;
    @OneToMany(mappedBy = "bookEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationEntity> reservations;




    public BookEntity(String author,
                      Boolean available,
                      String isbn,
                      String title,
                      String genre,
                      Integer numOfTotalCopies,
                      Integer numOfCopiesAvailable,
                      String coverImageUrl) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.genre = genre;
        this.available = available;
        this.numOfTotalCopies = numOfTotalCopies;
        this.numOfCopiesAvailable = numOfCopiesAvailable;
        this.coverImageUrl = coverImageUrl;
    }

    public BookEntity() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Boolean isAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Integer getNumOfTotalCopies() {
        return numOfTotalCopies;
    }

    public Integer getNumOfCopiesAvailable() {
        return numOfCopiesAvailable;
    }

    public void setNumOfTotalCopies(Integer numOfTotalCopies) {
        this.numOfTotalCopies = numOfTotalCopies;
    }

    public void setNumOfCopiesAvailable(Integer numOfCopiesAvailable) {
        this.numOfCopiesAvailable = numOfCopiesAvailable;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl){
        this.coverImageUrl = coverImageUrl;
    }

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

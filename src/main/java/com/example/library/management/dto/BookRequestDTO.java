package com.example.library.management.dto;

public class BookRequestDTO {
    private String title;
    private String author;
    private String isbn;
    private String genre;
    private Integer numOfTotalCopies;
    private Integer numOfCopiesAvailable;
    private String coverImageUrl;
    private Boolean available;


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

    public Integer getNumOfTotalCopies() {
        return numOfTotalCopies;
    }

    public void setNumOfTotalCopies(Integer numOfTotalCopies) {
        this.numOfTotalCopies = numOfTotalCopies;
    }

    public Integer getNumOfCopiesAvailable() {
        return numOfCopiesAvailable;
    }

    public void setNumOfCopiesAvailable(Integer numOfCopiesAvailable) {
        this.numOfCopiesAvailable = numOfCopiesAvailable;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}

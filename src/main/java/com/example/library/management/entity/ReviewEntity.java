package com.example.library.management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
public class ReviewEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int rating;
    @Column(length = 1000)
    private String comment;
    @ManyToOne
    @JoinColumn(name = "book-entity-id")
    private Book bookEntity;
    @ManyToOne
    @JoinColumn(name = "user-entity-id")
    private UserEntity userEntity;
    private LocalDateTime createdAt;

    public ReviewEntity(int rating, String comment, Book bookEntity, UserEntity userEntity, LocalDateTime createdAt) {
        this.rating = rating;
        this.comment = comment;
        this.bookEntity = bookEntity;
        this.userEntity = userEntity;
        this.createdAt = createdAt;
    }

    public ReviewEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Book getBookEntity() {
        return bookEntity;
    }

    public void setBookEntity(Book bookEntity) {
        this.bookEntity = bookEntity;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ReviewEntity{" +
                "rating=" + rating +
                ", comment='" + comment + '\'' +
                ", bookEntity=" + bookEntity.title +
                ", userEntity=" + userEntity.getName() +
                ", createdAt=" + createdAt +
                '}';
    }
}

package com.example.library.management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    public Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;

    @Column(name = "rating")
    public int rating;

    @Column(length = 1000, name = "comment")
    public String comment;

    @Column(name = "created_at")
    public LocalDateTime createdAt;

    public Review() {}

    public String toString() {
        return "ReviewEntity{" +
                "rating=" + rating +
                ", comment='" + comment + '\'' +
                ", bookEntity=" + book.title +
                ", userEntity=" + user.name +
                ", createdAt=" + createdAt +
                '}';
    }
}

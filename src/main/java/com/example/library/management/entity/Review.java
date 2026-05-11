package com.example.library.management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reviews")
public class Review extends BaseEntity
{
    @Override
    public String getIdPrefix() {
        return "REV-";
    }

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

    public Review() {}

    public String toString() {
        return "ReviewEntity{" +
                "rating=" + rating +
                ", comment='" + comment + '\'' +
                ", bookEntity=" + book.title +
                ", userEntity=" + user.name +
                ", createdAt=" + created +
                '}';
    }
}

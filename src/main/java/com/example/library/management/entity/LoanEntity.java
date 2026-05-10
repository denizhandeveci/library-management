package com.example.library.management.entity;

import jakarta.persistence.*;


import java.time.LocalDate;

@Entity
@Table(name="loan_entity")
public class LoanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_entity_id")
    private Book bookEntity;
    @ManyToOne
    private UserEntity userEntity;

    @Column(name="loan_date")
    private LocalDate loanDate;
    @Column(name="due_date")
    private LocalDate dueDate;
    @Column(name="return_date")
    private LocalDate returnDate;
    @Column(name="is_returned")
    private boolean isReturned = false;

    public LoanEntity(Book bookEntity, UserEntity userEntity, LocalDate loanDate, LocalDate dueDate, boolean isReturned, LocalDate returnDate) {
        this.bookEntity = bookEntity;
        this.userEntity = userEntity;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.isReturned = isReturned;
        this.returnDate = returnDate;
    }

    public LoanEntity(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean returned) {
        this.isReturned = returned;
    }
}

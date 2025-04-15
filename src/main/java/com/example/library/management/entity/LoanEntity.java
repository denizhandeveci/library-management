package com.example.library.management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.User;

import java.awt.print.Book;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private BookEntity bookEntity;

    @ManyToOne
    private UserEntity userEntity;

    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    private boolean isReturned;


}

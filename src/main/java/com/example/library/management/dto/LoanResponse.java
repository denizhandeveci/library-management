package com.example.library.management.dto;

import com.example.library.management.entity.Loan;

import java.time.LocalDate;

public record LoanResponse(
        Long id,
        Long bookId,

        String bookTitle,
        String author,
        String genre,
        String isbn,

        Long userId,
        String userName,

        LocalDate loanDate,
        LocalDate dueDate,
        LocalDate returnDate,

        boolean isReturned
)
{
    public static LoanResponse fromEntity(Loan loan) {
        var book = loan.book;
        var user = loan.user;

        return new LoanResponse(
                loan.id,

                book.id,
                book.title,
                book.author,
                book.genre,
                book.isbn,

                user.id,
                user.name,

                loan.loanDate,
                loan.dueDate,
                loan.returnDate,
                loan.isReturned()
        );
    }
}

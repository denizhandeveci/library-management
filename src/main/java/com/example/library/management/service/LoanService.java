package com.example.library.management.service;

import com.example.library.management.entity.BookEntity;
import com.example.library.management.entity.LoanEntity;
import com.example.library.management.repository.BookRepository;
import com.example.library.management.repository.LoanRepository;
import com.example.library.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Autowired
    public LoanService(LoanRepository loanRepository,BookRepository bookRepository, UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }


    public LoanEntity createLoan(Long bookId, Long userId){
        LoanEntity loanEntity = new LoanEntity();
        loanEntity.setLoanDate(LocalDate.now());
        loanEntity.setDueDate(LocalDate.now().plusWeeks(2));
        loanEntity.setReturned(false);
        loanEntity.setReturnDate(null);
        BookEntity bookEntity = bookRepository.findById(bookId).orElseThrow();
        bookEntity.setAvailable(false);
        loanEntity.setBookEntity(bookRepository.findById(bookId).orElseThrow());
        loanEntity.setUserEntity(userRepository.findById(userId).orElseThrow());
        return loanRepository.save(loanEntity);
    }
}

package com.example.library.management.service;

import com.example.library.management.entity.BookEntity;
import com.example.library.management.entity.LoanEntity;
import com.example.library.management.entity.UserEntity;
import com.example.library.management.repository.BookRepository;
import com.example.library.management.repository.LoanRepository;
import com.example.library.management.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
        BookEntity bookEntity = bookRepository.findById(bookId).orElseThrow();


        if(bookEntity.isAvailable()){
            LoanEntity loanEntity = new LoanEntity();
            loanEntity.setLoanDate(LocalDate.now());
            loanEntity.setDueDate(LocalDate.now().plusWeeks(2));
            loanEntity.setReturned(false);
            loanEntity.setReturnDate(null);
            bookEntity.setNumOfCopies(bookEntity.getNumOfCopies()-1);
            if(bookEntity.getNumOfCopies() == 0){
                bookEntity.setAvailable(false);
            }


            loanEntity.setBookEntity(bookRepository.findById(bookId).orElseThrow());
            loanEntity.setUserEntity(userRepository.findById(userId).orElseThrow());
            return loanRepository.save(loanEntity);

        }else{
            throw new IllegalStateException("Book with ID " + bookId + " is currently not available for loan.");
        }

    }

    public void returnLoan(Long userId, Long bookId){
        LoanEntity loanEntity = loanRepository.findByUserEntityIdAndBookEntityIdAndIsReturnedFalse(userId,bookId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if(loanEntity.isReturned()){
            throw new IllegalStateException("Book has already been returned");
        }
        loanEntity.setReturned(true);
        loanEntity.setReturnDate(LocalDate.now());
        BookEntity bookEntity = loanEntity.getBookEntity();
        bookEntity.setAvailable(true);
        loanRepository.save(loanEntity);
    }
}

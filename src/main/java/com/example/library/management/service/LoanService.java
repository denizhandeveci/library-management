package com.example.library.management.service;

import com.example.library.management.entity.BookEntity;
import com.example.library.management.entity.LoanEntity;
import com.example.library.management.entity.UserEntity;
import com.example.library.management.repository.BookRepository;
import com.example.library.management.repository.LoanRepository;
import com.example.library.management.repository.ReservationRepository;
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
    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;

    @Autowired
    public LoanService(LoanRepository loanRepository,
                       BookRepository bookRepository,
                       UserRepository userRepository,
                       ReservationRepository reservationRepository,
                       ReservationService reservationService) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
        this.reservationService = reservationService;
    }


    public LoanEntity createLoan(Long bookId, Long userId) {
        BookEntity bookEntity = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + bookId));

        boolean loanedAndNotReturned = loanRepository.existsByBookEntityIdAndUserEntityIdAndIsReturnedFalse(bookId, userId);

        if (loanedAndNotReturned) {
            throw new IllegalStateException("User with ID " + userId + " has already loaned this book and not returned it yet.");
        }

        if (!bookEntity.isAvailable()) {
            reservationService.makeReservation(userId,bookId);
            throw new IllegalStateException("Book is currently not available. Reservation has been made.");
        }else{
            LoanEntity loanEntity = new LoanEntity();
            loanEntity.setLoanDate(LocalDate.now());
            loanEntity.setDueDate(LocalDate.now().plusWeeks(2));
            loanEntity.setReturned(false);
            loanEntity.setReturnDate(null);
            bookEntity.setNumOfCopies(bookEntity.getNumOfCopies() - 1);

            if (bookEntity.getNumOfCopies() == 0) {
                bookEntity.setAvailable(false);
            }
            loanEntity.setBookEntity(bookEntity);
            loanEntity.setUserEntity(userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId)));
            return loanRepository.save(loanEntity);
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
        bookEntity.setNumOfCopies(bookEntity.getNumOfCopies()+1);
//        if(reservationRepository.findById(bookId).isPresent()){
//            createLoan(bookId,reservationRepository.findById(bookId)::map)
//        }
        loanRepository.save(loanEntity);
    }
}

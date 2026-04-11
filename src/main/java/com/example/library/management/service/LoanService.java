package com.example.library.management.service;

import com.example.library.management.entity.BookEntity;
import com.example.library.management.entity.LoanEntity;
import com.example.library.management.entity.ReservationEntity;
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
import java.util.Optional;

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
            bookEntity.setNumOfCopiesAvailable(bookEntity.getNumOfCopiesAvailable() - 1);

            if (bookEntity.getNumOfCopiesAvailable() == 0) {
                bookEntity.setAvailable(false);
            }
            loanEntity.setBookEntity(bookEntity);
            loanEntity.setUserEntity(userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId)));
            return loanRepository.save(loanEntity);
        }
    }


    public void returnLoan(Long userId, Long bookId){

        // Finds the active loan. If the user loaned same book before and returned it we don't want it we want the active loan
        LoanEntity loanEntity = loanRepository.findByUserEntityIdAndBookEntityIdAndIsReturnedFalse(userId,bookId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
        // if the book is already returned throw an exception and say it has already been returned.
        if(loanEntity.isReturned()){
            throw new IllegalStateException("Book has already been returned");
        }
        // Else i set necessary fields
        loanEntity.setReturned(true);
        loanEntity.setReturnDate(LocalDate.now());
        BookEntity bookEntity = loanEntity.getBookEntity();
        bookEntity.setAvailable(true);
        bookEntity.setNumOfCopiesAvailable(bookEntity.getNumOfCopiesAvailable()+1);
        //bookRepository.save(bookEntity);
        // Here i write a query to find the oldest reservation
        // in other words if multiple reservations are created by separate users for the same book
        // once the book is available again the first user that reserved the book will be able to loan it.
        Optional<ReservationEntity> oldestReservation =
                reservationRepository.findFirstByBookEntityIdOrderByIdAsc(bookId);
        // Here is a simple if statement where i check if someone is waiting for this book
        // if yes then i grab that reservation
        if (oldestReservation.isPresent()) {
            ReservationEntity reservationEntity = oldestReservation.get();

            // here off of the oldest reservation i grab the userId and the id of the reservation
            Long reservedUserId = reservationEntity.getUserId().getId();
            // here i call the createLoan function i defined previously to create a loan
            createLoan(bookId, reservedUserId);
            // and finally i delete the reservation
            reservationRepository.delete(reservationEntity);
        }
        loanRepository.save(loanEntity);
        }
}

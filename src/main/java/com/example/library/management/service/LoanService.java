package com.example.library.management.service;

import com.example.library.management.dto.LoanRequest;
import com.example.library.management.dto.LoanResponse;
import com.example.library.management.dto.ReservationRequest;
import com.example.library.management.entity.Book;
import com.example.library.management.entity.Loan;
import com.example.library.management.entity.Reservation;
import com.example.library.management.entity.User;
import com.example.library.management.repository.BookRepository;
import com.example.library.management.repository.LoanRepository;
import com.example.library.management.repository.ReservationRepository;
import com.example.library.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService
{
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
                       ReservationService reservationService)
    {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
        this.reservationService = reservationService;
    }

    public List<LoanResponse> findAllLoans(Long userId) {
        List<Loan> loans = this.loanRepository.findOpenLoansByUserId(userId);

        return loans.stream()
                .map(LoanResponse::fromEntity)
                .toList();
    }

    public LoanResponse createLoan(LoanRequest loanRequest) {
        // TODO implement an interceptor or handle gracefully, don't throw a 5XX, yb
        Book book = bookRepository.findById(loanRequest.bookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        User user = userRepository.findById(loanRequest.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));


        boolean loanedAndNotReturned = loanRepository.existsOpenLoanByUserIdAndBookId(book.id, user.id);
        if (loanedAndNotReturned) {
            throw new IllegalStateException("User with ID " + user.id + " has already loaned this book and not returned it yet.");
        }


        if (!book.isAvailable()) {
            ReservationRequest reservationRequest = new ReservationRequest(
                    user.id,
                    book.id
            );

            reservationService.makeReservation(reservationRequest.userId(), reservationRequest.bookId());

            // TODO don't throw here but instead handle gracefully?, yb
            throw new IllegalStateException("Book is currently not available. Reservation has been made.");
        }

        book.numOfCopiesAvailable -= 1;

        Loan loanEntity = createLoanEntity(book, user);
        Loan savedLoan = loanRepository.save(loanEntity);
        return LoanResponse.fromEntity(savedLoan);
    }


    public void returnLoan(Long userId, Long bookId) {

        // Finds the active loan. If the user loaned the same book before and returned it, we don't want it we want the active loan
        Loan loan = loanRepository.findOpenLoanByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        // if the book is already returned, throw an exception and say it has already been returned.
        if (loan.isReturned()) {
            throw new IllegalStateException("Book has already been returned");
        }
        // Else I set necessary fields
        loan.returnDate = LocalDate.now();

        Book bookEntity = loan.book;

        bookEntity.numOfCopiesAvailable += 1;

        //bookRepository.save(bookEntity);
        // Here I write a query to find the oldest reservation
        // in other words, if multiple reservations are created by separate users for the same book
        // once the book is available again, the first user that reserved the book will be able to loan it.
        Optional<Reservation> oldestReservation = findFirstReservation(bookId);

        // Here is a simple if statement where I check if someone is waiting for this book
        // if yes, then I grab that reservation
        if (oldestReservation.isPresent()) {
            Reservation reservationEntity = oldestReservation.get();

            // here off of the oldest reservation I grab the userId and the id of the reservation
            Long reservedUserId = reservationEntity.user.id;

            // here I call the createLoan function I defined previously to create a loan
            LoanRequest dto = new LoanRequest(
                    bookId,
                    reservedUserId
            );

            createLoan(dto);
            // and finally I delete the reservation
            reservationRepository.delete(reservationEntity);
        }
        loanRepository.save(loan);
    }

    public Optional<Reservation> findFirstReservation(Long bookId) {
        return reservationRepository
                .findActiveReservationsByBookId(bookId, PageRequest.of(0, 1))
                .stream().findFirst();
    }

    public Loan createLoanEntity(Book book, User user) {
        Loan loan = new Loan();

        loan.book = book;
        loan.user = user;

        loan.loanDate = LocalDate.now();
        loan.dueDate = LocalDate.now().plusWeeks(2);

        loan.returnDate = null;

        return loan;
    }
}

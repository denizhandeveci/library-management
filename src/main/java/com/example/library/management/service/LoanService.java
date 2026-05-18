package com.example.library.management.service;

import com.example.library.management.dto.LoanRequest;
import com.example.library.management.dto.LoanResponse;
import com.example.library.management.dto.ReservationRequest;
import com.example.library.management.entity.Book;
import com.example.library.management.entity.Loan;
import com.example.library.management.entity.Reservation;
import com.example.library.management.entity.User;
import com.example.library.management.exception.ConflictException;
import com.example.library.management.exception.ResourceNotFoundException;
import com.example.library.management.repository.BookRepository;
import com.example.library.management.repository.LoanRepository;
import com.example.library.management.repository.ReservationRepository;
import com.example.library.management.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService
{
    private static final Logger log = LoggerFactory.getLogger(LoanService.class);

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

        log.debug("Found {} open loans for userId={}", loans.size(), userId);

        return loans.stream()
                .map(LoanResponse::fromEntity)
                .toList();
    }

    @Transactional
    public LoanResponse createLoan(LoanRequest loanRequest) {
        Long bookId = loanRequest.bookId();
        Long userId = loanRequest.userId();

        log.info("Creating loan for userId={} and bookId={}", userId, bookId);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> ResourceNotFoundException.forId("Book", bookId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ResourceNotFoundException.forId("User", userId));

        boolean loanedAndNotReturned = loanRepository.existsOpenLoanByUserIdAndBookId(user.id, book.id);
        if (loanedAndNotReturned) {
            throw new ConflictException("User with id=" + user.id + " already has an open loan for bookId=" + book.id + ".");
        }

        if (!book.isAvailable()) {
            log.info("BookId={} is not available. Creating reservation for userId={}", book.id, user.id);

            ReservationRequest reservationRequest = new ReservationRequest(
                    user.id,
                    book.id
            );

            reservationService.makeReservation(reservationRequest.userId(), reservationRequest.bookId());

            log.info("Reservation created instead of loan for userId={} and bookId={}", user.id, book.id);
        }

        book.numOfCopiesAvailable -= 1;
        bookRepository.save(book);

        Loan loanEntity = createLoanEntity(book, user);
        Loan savedLoan = loanRepository.save(loanEntity);

        log.info(
                "Loan created successfully with loanId={} for userId={} and bookId={}. Available copies now={}",
                savedLoan.id,
                user.id,
                book.id,
                book.numOfCopiesAvailable
        );

        return LoanResponse.fromEntity(savedLoan);
    }

    @Transactional
    public void returnLoan(Long loanId) {
        log.info("Returning loan for loanId={}", loanId);

        // Finds the active loan. If the user loaned the same book before and returned it, we don't want it we want the active loan
        Loan loan = loanRepository.findOpenLoanById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Open loan for loanId= " + loanId + " was not found."));

        if (loan.isReturned()) {
            throw new ConflictException("Loan=" + loanId + " has already been returned.");
        }

        // Else I set necessary fields
        loan.returnDate = LocalDate.now();

        Book bookEntity = loan.book;

        bookEntity.numOfCopiesAvailable += 1;

        log.info(
                "Loan marked as returned with loanId={} for userId={} and bookId={}. Available copies now={}",
                loan.id,
                loan.user.id,
                loan.book.id,
                bookEntity.numOfCopiesAvailable
        );

        // Here I write a query to find the oldest reservation
        // in other words, if multiple reservations are created by separate users for the same book
        // once the book is available again, the first user that reserved the book will be able to loan it.
        Optional<Reservation> oldestReservation = findFirstReservation(loan.book.id);

        // Here is a simple if statement where I check if someone is waiting for this book
        // if yes, then I grab that reservation
        if (oldestReservation.isPresent()) {
            Reservation reservation = oldestReservation.get();
            // here off of the oldest reservation I grab the userId and the id of the reservation
            Long reservedUserId = reservation.user.id;

            log.info(
                    "Found active reservationId={} for returned bookId={}. Creating loan for reservedUserId={}",
                    reservation.id,
                    loan.book.id,
                    reservedUserId
            );

            // here I call the createLoan function I defined previously to create a loan
            LoanRequest dto = new LoanRequest(
                    loan.book.id,
                    reservedUserId
            );

            createLoan(dto);
            // and finally I delete the reservation
            reservation.softDelete();

            log.info(
                    "ReservationId={} fulfilled and soft deleted for reservedUserId={} and bookId={}",
                    reservation.id,
                    reservedUserId,
                    loan.book.id
            );
        } else {
            log.debug("No active reservation found for returned bookId={}", loan.book.id);
        }

        loanRepository.save(loan);
    }

    public Optional<Reservation> findFirstReservation(Long bookId) {
        Optional<Reservation> reservation = reservationRepository
                .findActiveReservationsByBookId(bookId, PageRequest.of(0, 1))
                .stream().findFirst();

        log.debug("Oldest active reservation lookup for bookId={} foundReservation={}", bookId, reservation.isPresent());

        return reservation;
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

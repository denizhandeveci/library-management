package com.example.library.management.service;

import com.example.library.management.dto.LoanRequestDTO;
import com.example.library.management.dto.LoanResponseDTO;
import com.example.library.management.dto.ReservationRequestDTO;
import com.example.library.management.entity.Book;
import com.example.library.management.entity.LoanEntity;
import com.example.library.management.entity.ReservationEntity;
import com.example.library.management.entity.UserEntity;
import com.example.library.management.repository.BookRepository;
import com.example.library.management.repository.LoanRepository;
import com.example.library.management.repository.ReservationRepository;
import com.example.library.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

    public List<LoanResponseDTO> findAllLoans(Long userId) {
        List<LoanEntity> loanEntities = this.loanRepository.findAllByUserEntityIdAndIsReturnedFalse(userId);
        List<LoanResponseDTO> loanResponseDTOS = new ArrayList<>();
        for(LoanEntity loanEntity : loanEntities) {
            loanResponseDTOS.add(mapToDTO(loanEntity));
        }
        return loanResponseDTOS;
    }

    public LoanResponseDTO createLoan(LoanRequestDTO loanRequestDTO) {

        LoanEntity loanEntity = mapToEntity(loanRequestDTO);
        Book book = loanEntity.getBookEntity();
        Long bookId = book.id;
        Long userId = loanEntity.getUserEntity().getId();

        boolean loanedAndNotReturned = loanRepository.existsByBookEntityIdAndUserEntityIdAndIsReturnedFalse(bookId, userId);

        if (loanedAndNotReturned) {
            throw new IllegalStateException("User with ID " + userId + " has already loaned this book and not returned it yet.");
        }

        if (!book.available) {
            ReservationRequestDTO reservationRequestDTO = new ReservationRequestDTO();
            reservationRequestDTO.setUserId(userId);
            reservationRequestDTO.setBookId(bookId);
            reservationService.makeReservation(reservationRequestDTO.getUserId(), reservationRequestDTO.getBookId());

            throw new IllegalStateException("Book is currently not available. Reservation has been made.");
        }
        book.numOfCopiesAvailable -= 1;

        book.available = book.numOfCopiesAvailable > 0;

        LoanEntity savedLoan = loanRepository.save(loanEntity);
        return mapToDTO(savedLoan);
        }



    public void returnLoan(Long userId, Long bookId){

        // Finds the active loan. If the user loaned the same book before and returned it, we don't want it we want the active loan
        LoanEntity loanEntity = loanRepository.findByUserEntityIdAndBookEntityIdAndIsReturnedFalse(userId,bookId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
        // if the book is already returned, throw an exception and say it has already been returned.
        if(loanEntity.isReturned()){
            throw new IllegalStateException("Book has already been returned");
        }
        // Else I set necessary fields
        loanEntity.setReturned(true);
        loanEntity.setReturnDate(LocalDate.now());
        Book bookEntity = loanEntity.getBookEntity();
        bookEntity.available = true;
        bookEntity.numOfCopiesAvailable += 1;
        //bookRepository.save(bookEntity);
        // Here I write a query to find the oldest reservation
        // in other words, if multiple reservations are created by separate users for the same book
        // once the book is available again, the first user that reserved the book will be able to loan it.
        Optional<ReservationEntity> oldestReservation =
                reservationRepository.findFirstByBookEntityIdOrderByIdAsc(bookId);
        // Here is a simple if statement where I check if someone is waiting for this book
        // if yes, then I grab that reservation
        if (oldestReservation.isPresent()) {
            ReservationEntity reservationEntity = oldestReservation.get();

            // here off of the oldest reservation I grab the userId and the id of the reservation
            Long reservedUserId = reservationEntity.getUserId().getId();
            // here I call the createLoan function I defined previously to create a loan
            LoanRequestDTO dto = new LoanRequestDTO();
            dto.setBookId(bookId);
            dto.setUserId(reservedUserId);

            createLoan(dto);
            // and finally I delete the reservation
            reservationRepository.delete(reservationEntity);
        }
        loanRepository.save(loanEntity);
        }

    public LoanResponseDTO mapToDTO(LoanEntity loanEntity){
        LoanResponseDTO dto = new LoanResponseDTO();

        dto.setId(loanEntity.getId());
        dto.setBookId(loanEntity.getBookEntity().id);
        dto.setBookTitle(loanEntity.getBookEntity().title);
        dto.setUserId(loanEntity.getUserEntity().getId());
        dto.setUserName(loanEntity.getUserEntity().getName());
        dto.setLoanDate(loanEntity.getLoanDate());
        dto.setDueDate(loanEntity.getDueDate());
        dto.setReturnDate(loanEntity.getReturnDate());
        dto.setReturned(loanEntity.isReturned());
        dto.setIsbn(loanEntity.getBookEntity().isbn);
        dto.setGenre(loanEntity.getBookEntity().genre);
        dto.setAuthor(loanEntity.getBookEntity().author);

        return dto;
    }

    public LoanEntity mapToEntity(LoanRequestDTO loanRequestDTO){
        Book book = bookRepository.findById(loanRequestDTO.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        UserEntity user = userRepository.findById(loanRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        LoanEntity loan = new LoanEntity();

        loan.setBookEntity(book);
        loan.setUserEntity(user);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusWeeks(2));

        loan.setReturned(false);
        loan.setReturnDate(null);

        return loan;
    }
}

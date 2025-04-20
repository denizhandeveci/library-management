package com.example.library.management.service;

import com.example.library.management.entity.BookEntity;
import com.example.library.management.entity.LoanEntity;
import com.example.library.management.entity.UserEntity;
import com.example.library.management.repository.BookRepository;
import com.example.library.management.repository.LoanRepository;
import com.example.library.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;

    @Autowired
    public UserService(UserRepository userRepository, BookRepository bookRepository,LoanRepository loanRepository){
        this.userRepository =userRepository;
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
    }

    public UserEntity createUser(UserEntity userEntity){
        return userRepository.save(userEntity);
    }


    public void deleteUser(Long userId){
        UserEntity userEntity = userRepository.findById(userId).orElseThrow();
        userRepository.delete(userEntity);


    }

//    public void returnBook(Long userId, Long bookId){
//        BookEntity bookEntity = bookRepository.findById(bookId).orElseThrow();
//        LoanEntity loanEntity = loanRepository.getLoanEntityByBookEntityIdAndUserEntityId(userId,bookId);
//        bookEntity.setAvailable(true);
//        loanEntity.setReturnDate(LocalDate.now());
//        loanEntity.setReturned(true);
//
//    }

}

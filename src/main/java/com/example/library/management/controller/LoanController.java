package com.example.library.management.controller;

import com.example.library.management.entity.LoanEntity;
import com.example.library.management.service.LoanService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService){
        this.loanService = loanService;
    }

    @PostMapping("/create-loan-userId-bookId/{userId}/{bookId}")
    public LoanEntity createLoan(@PathVariable Long userId, @PathVariable Long bookId){
        return loanService.createLoan(bookId, userId);
    }

    @PostMapping("/return-book/{userId}/{bookId}")
    public void returnLoan(@PathVariable Long userId, @PathVariable Long bookId) {
        loanService.returnLoan(userId, bookId);
    }

}

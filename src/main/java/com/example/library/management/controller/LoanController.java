package com.example.library.management.controller;

import com.example.library.management.dto.LoanRequest;
import com.example.library.management.dto.LoanResponse;
import com.example.library.management.service.LoanService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService){
        this.loanService = loanService;
    }

    @PostMapping("/create-loan-userId-bookId/{userId}/{bookId}")
    public LoanResponse createLoan(@PathVariable Long userId,
                                   @PathVariable Long bookId){

        // TODO clarify/discuss: should be refactored to accept the request in the body?, yb
        LoanRequest loanRequestDTO = new LoanRequest(
                userId,
                bookId
        );
        return loanService.createLoan(loanRequestDTO);
    }

    @PostMapping("/return-book/{userId}/{bookId}")
    public void returnLoan(@PathVariable Long userId, @PathVariable Long bookId) {
        loanService.returnLoan(userId, bookId);
    }

    @GetMapping("/get-loans/{userId}")
    public List<LoanResponse> getLoans(@PathVariable Long userId) {
        return loanService.findAllLoans(userId);
    }

}

package com.example.library.management.controller;

import com.example.library.management.dto.LoanRequest;
import com.example.library.management.dto.LoanResponse;
import com.example.library.management.service.LoanService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LoanController
{

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/loans")
    public LoanResponse createLoan(@RequestBody LoanRequest loanRequest)
    {
        // TODO clarify/discuss: should be refactored to accept the request in the body?, yb
        LoanRequest loanRequestDTO = new LoanRequest(loanRequest.bookId(), loanRequest.userId());
        return loanService.createLoan(loanRequestDTO);
    }

    @PostMapping("/loans/return?")
    public void returnLoan(@RequestBody LoanRequest loanRequest) {
        loanService.returnLoan(loanRequest.userId(), loanRequest.bookId());
    }

    @GetMapping("/loans/{userId}")
    public List<LoanResponse> getLoans(@PathVariable Long userId) {
        return loanService.findAllLoans(userId);
    }

}

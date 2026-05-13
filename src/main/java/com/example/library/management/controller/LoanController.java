package com.example.library.management.controller;

import com.example.library.management.dto.LoanRequest;
import com.example.library.management.dto.LoanResponse;
import com.example.library.management.service.LoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LoanController
{
    private static final Logger log = LoggerFactory.getLogger(LoanController.class);

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping("/loans/{userId}")
    public List<LoanResponse> getLoans(@PathVariable Long userId) {
        log.debug("Received get loans request for userId={}", userId);

        return loanService.findAllLoans(userId);
    }

    @PostMapping("/loans")
    public LoanResponse createLoan(@RequestBody LoanRequest loanRequest)
    {
        log.debug("Received create loan request for userId={} and bookId={}", loanRequest.userId(), loanRequest.bookId());

        // TODO clarify/discuss: should be refactored to accept the request in the body?, yb
        LoanRequest loanRequestDTO = new LoanRequest(
                loanRequest.bookId(),
                loanRequest.userId()
        );
        return loanService.createLoan(loanRequestDTO);
    }

    @PostMapping("/loans/return")
    public void returnLoan(@RequestBody LoanRequest loanRequest) {
        log.debug("Received return loan request for userId={} and bookId={}", loanRequest.userId(), loanRequest.bookId());

        loanService.returnLoan(loanRequest.userId(), loanRequest.bookId());
    }



}

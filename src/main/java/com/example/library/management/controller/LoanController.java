package com.example.library.management.controller;

import com.example.library.management.dto.LoanRequest;
import com.example.library.management.dto.LoanResponse;
import com.example.library.management.service.LoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoanController
{
    private static final Logger log = LoggerFactory.getLogger(LoanController.class);

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/create-loan-userId-bookId/{userId}/{bookId}")
    public LoanResponse createLoan(@PathVariable Long userId,
                                   @PathVariable Long bookId)
    {
        log.debug("Received create loan request for userId={} and bookId={}", userId, bookId);

        // TODO clarify/discuss: should be refactored to accept the request in the body?, yb
        LoanRequest loanRequestDTO = new LoanRequest(
                bookId,
                userId
        );
        return loanService.createLoan(loanRequestDTO);
    }

    @PostMapping("/return-book/{userId}/{bookId}")
    public void returnLoan(@PathVariable Long userId, @PathVariable Long bookId) {
        log.debug("Received return loan request for userId={} and bookId={}", userId, bookId);

        loanService.returnLoan(userId, bookId);
    }

    @GetMapping("/get-loans/{userId}")
    public List<LoanResponse> getLoans(@PathVariable Long userId) {
        log.debug("Received get loans request for userId={}", userId);

        return loanService.findAllLoans(userId);
    }

}

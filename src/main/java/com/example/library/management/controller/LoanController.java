package com.example.library.management.controller;

import com.example.library.management.dto.LoanRequest;
import com.example.library.management.dto.LoanResponse;
import com.example.library.management.service.LoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping("/loans/{userId}")
    public List<LoanResponse> getLoans(@PathVariable Long userId) {
        return loanService.findAllLoans(userId);
    }

    @PostMapping("/loans")
    public LoanResponse createLoan(@RequestBody LoanRequest request)
    {
        log.debug("Received create loan request for userId={} and bookId={}", request.userId(), request.bookId());

        return loanService.createLoan(request);
    }

    @PostMapping("/loans/{loanId}/return")
    public void returnLoan(@PathVariable Long loanId) {
        loanService.returnLoan(loanId);
    }
}

package com.example.library.management.controller;

import com.example.library.management.dto.LoanRequestDTO;
import com.example.library.management.dto.LoanResponseDTO;
import com.example.library.management.entity.LoanEntity;
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
    public LoanResponseDTO createLoan(@PathVariable Long userId,
                                      @PathVariable Long bookId){
        LoanRequestDTO loanRequestDTO = new LoanRequestDTO();
        loanRequestDTO.setUserId(userId);
        loanRequestDTO.setBookId(bookId);
        return loanService.createLoan(loanRequestDTO);
    }

    @PostMapping("/return-book/{userId}/{bookId}")
    public void returnLoan(@PathVariable Long userId, @PathVariable Long bookId) {
        loanService.returnLoan(userId, bookId);
    }

    @GetMapping("/get-loans/{userId}")
    public List<LoanResponseDTO> getLoans(@PathVariable Long userId) {
        return loanService.findAllLoans(userId);
    }

}

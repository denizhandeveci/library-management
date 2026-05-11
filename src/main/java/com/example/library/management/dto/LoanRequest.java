package com.example.library.management.dto;

public record LoanRequest(
        Long bookId,
        Long userId
) {}
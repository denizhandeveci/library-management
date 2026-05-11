package com.example.library.management.dto;

public record ReviewRequest(
        int rating,
        String comment,

        Long bookId,
        Long userId
) {}

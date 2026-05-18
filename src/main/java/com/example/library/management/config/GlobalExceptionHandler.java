package com.example.library.management.config;

import com.example.library.management.exception.BadRequestException;
import com.example.library.management.exception.ConflictException;
import com.example.library.management.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(
            ResourceNotFoundException exception,
            HttpServletRequest request
    )
    {
        log.warn(
                "Resource not found: method={}, uri={}, message={}",
                request.getMethod(),
                request.getRequestURI(),
                exception.getMessage()
        );

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                exception.getMessage()
        );

        problemDetail.setTitle("Resource not found");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return problemDetail;
    }

    @ExceptionHandler(ConflictException.class)
    public ProblemDetail handleConflict(
            ConflictException exception,
            HttpServletRequest request
    )
    {
        log.warn("Conflict: method={}, uri={}, message={}", request.getMethod(), request.getRequestURI(), exception.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                exception.getMessage()
        );

        problemDetail.setTitle("Conflict");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return problemDetail;
    }

    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail handleBadRequest(
            BadRequestException exception,
            HttpServletRequest request
    )
    {
        log.warn(
                "Bad request: method={}, uri={}, message={}",
                request.getMethod(),
                request.getRequestURI(),
                exception.getMessage()
        );

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        );

        problemDetail.setTitle("Bad request");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return problemDetail;
    }
}

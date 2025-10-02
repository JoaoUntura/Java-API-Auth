package dev.joaountura.auth.exceptions;

import dev.joaountura.auth.exceptions.authExceptions.TooManyRequests;
import dev.joaountura.auth.exceptions.dtos.ExceptionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionDto> handleBadCredentialsException(BadCredentialsException ex) {
        logger.warn("BadCredentialsException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionDto("Invalid email or password"));
    }

    @ExceptionHandler(TooManyRequests.class)
    public ResponseEntity<ExceptionDto> handleTooManyRequests(TooManyRequests ex) {
        logger.error("TooManyRequests: {}", ex.getMessage()); //
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(new ExceptionDto("Too Many attempts, try again latter"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> handleGeneric(Exception ex) {
        logger.error("Exception", ex); //
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionDto("Internal server error"));
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        logger.error("HttpMessageNotReadable", ex.getMessage()); //
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDto("Invalid JSON Body"));
    }


}

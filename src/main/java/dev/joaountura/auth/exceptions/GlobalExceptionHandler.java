package dev.joaountura.auth.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex){

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");

    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationExceptionA(DataIntegrityViolationException ex){

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Data");

    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex){

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getLocalizedMessage());

    }


}

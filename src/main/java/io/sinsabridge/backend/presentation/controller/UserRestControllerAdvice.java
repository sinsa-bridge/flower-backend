package io.sinsabridge.backend.presentation.controller;

import io.sinsabridge.backend.presentation.exception.UserNotFoundException;
import io.sinsabridge.backend.presentation.resource.ErrorResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserRestControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResource> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorResource error = new ErrorResource("User not found", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


}

package io.sinsabridge.backend.presentation.exception;// UserAlreadyExistsException.java

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}

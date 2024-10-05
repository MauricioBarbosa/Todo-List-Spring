package com.springtodo.core.autentication.domain.exception;

public class UserNotFoundException extends IllegalArgumentException {
    public UserNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}

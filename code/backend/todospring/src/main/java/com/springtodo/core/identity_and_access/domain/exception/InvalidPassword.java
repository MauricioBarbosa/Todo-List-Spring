package com.springtodo.core.identity_and_access.domain.exception;

public class InvalidPassword extends Exception {

    public InvalidPassword(String errorMessage) {
        super(errorMessage);
    }
}

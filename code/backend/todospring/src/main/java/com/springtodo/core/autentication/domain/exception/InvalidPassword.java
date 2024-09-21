package com.springtodo.core.autentication.domain.exception;

public class InvalidPassword extends Exception {
    public InvalidPassword(String errorMessage) {
        super(errorMessage);
    }
}
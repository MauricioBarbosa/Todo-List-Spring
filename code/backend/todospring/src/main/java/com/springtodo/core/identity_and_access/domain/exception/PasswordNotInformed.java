package com.springtodo.core.autentication.domain.exception;

public class PasswordNotInformed extends IllegalArgumentException {
    public PasswordNotInformed(String errorMessage) {
        super(errorMessage);
    }
}

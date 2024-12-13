package com.springtodo.core.identity_and_access.domain.exception;

public class PasswordNotInformed extends IllegalArgumentException {

    public PasswordNotInformed(String errorMessage) {
        super(errorMessage);
    }
}

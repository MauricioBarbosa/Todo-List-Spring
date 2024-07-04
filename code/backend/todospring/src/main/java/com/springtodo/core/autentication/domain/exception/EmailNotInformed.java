package com.springtodo.core.autentication.domain.exception;

public class EmailNotInformed extends IllegalArgumentException {
    public EmailNotInformed(String errorMessage) {
        super(errorMessage);
    }
}

package com.springtodo.core.identity_and_access.domain.exception;

public class EmailNotInformed extends IllegalArgumentException {

    public EmailNotInformed(String errorMessage) {
        super(errorMessage);
    }
}

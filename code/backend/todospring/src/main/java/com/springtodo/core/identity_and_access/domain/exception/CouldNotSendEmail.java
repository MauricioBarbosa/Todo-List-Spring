package com.springtodo.core.autentication.domain.exception;

public class CouldNotSendEmail extends Exception {
    public CouldNotSendEmail(String errorMessage) {
        super(errorMessage);
    }
}

package com.springtodo.core.autentication.domain.exception;

public class CouldNotCreateSession extends IllegalArgumentException {
    public CouldNotCreateSession(String errorMessage) {
        super(errorMessage);
    }
}

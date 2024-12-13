package com.springtodo.core.identity_and_access.domain.exception;

public class CouldNotSaveSession extends Exception {
    public CouldNotSaveSession(String errorMessage) {
        super(errorMessage);
    }
}

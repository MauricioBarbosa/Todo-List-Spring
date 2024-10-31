package com.springtodo.core.identity_and_access.domain.exception;

public class CouldNotFindSession extends Exception {

    public CouldNotFindSession(String errorMessage) {
        super(errorMessage);
    }
}

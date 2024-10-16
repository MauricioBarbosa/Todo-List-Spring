package com.springtodo.core.identity_and_access.domain.exception;

public class CouldNotCreateSession extends Error {

    public CouldNotCreateSession(String errorMessage) {
        super(errorMessage);
    }
}

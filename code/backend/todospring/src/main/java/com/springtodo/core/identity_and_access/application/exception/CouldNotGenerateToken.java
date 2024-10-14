package com.springtodo.core.identity_and_access.application.exception;

public class CouldNotGenerateToken extends Exception {

    public CouldNotGenerateToken(String errorMessage) {
        super(errorMessage);
    }
}

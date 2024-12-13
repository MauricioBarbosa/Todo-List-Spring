package com.springtodo.core.identity_and_access.application.exception;

public class CouldNotDecodeToken extends Exception {

    public CouldNotDecodeToken(String errorMessage) {
        super(errorMessage);
    }
}

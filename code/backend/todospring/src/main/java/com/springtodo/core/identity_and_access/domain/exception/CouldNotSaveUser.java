package com.springtodo.core.identity_and_access.domain.exception;

public class CouldNotSaveUser extends Exception {
    public CouldNotSaveUser(String errorMessage) {
        super(errorMessage);
    }
}

package com.springtodo.core.autentication.domain.exception;

public class CouldNotRetrieveUser extends Exception {
    public CouldNotRetrieveUser(String errorMessage) {
        super(errorMessage);
    }
}

package com.springtodo.core.identity_and_access.domain.exception;



public class CouldNotRetrieveUser extends Exception {
    public CouldNotRetrieveUser(String errorMessage) {
        super(errorMessage);
    }
}

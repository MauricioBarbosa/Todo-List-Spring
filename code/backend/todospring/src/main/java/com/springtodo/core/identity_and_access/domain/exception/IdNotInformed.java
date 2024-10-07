package com.springtodo.core.identity_and_access.domain.exception;

public class IdNotInformed extends IllegalArgumentException {

    public IdNotInformed(String errorMessage) {
        super(errorMessage);
    }
}

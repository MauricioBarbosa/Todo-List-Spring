package com.springtodo.core.autentication.domain.exception;

public class IdNotInformed extends IllegalArgumentException {
    public IdNotInformed(String errorMessage) {
        super(errorMessage);
    }
}

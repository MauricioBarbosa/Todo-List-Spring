package com.springtodo.core.identity_and_access.domain.exception;

public class InvalidSession extends Exception {
    public InvalidSession() {
        super("invalid session");
    }
}

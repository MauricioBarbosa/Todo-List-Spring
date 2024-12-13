package com.springtodo.core.identity_and_access.application.exception;

public class InvalidToken extends Exception {

    public InvalidToken() {
        super("invalid token");
    }
}

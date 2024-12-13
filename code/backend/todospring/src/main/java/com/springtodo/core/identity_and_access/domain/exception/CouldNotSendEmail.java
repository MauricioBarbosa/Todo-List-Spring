package com.springtodo.core.identity_and_access.domain.exception;

public class CouldNotSendEmail extends Error {

    public CouldNotSendEmail(String message) {
        super(message);
    }
}

package com.springtodo.core.identity_and_access.domain.exception;

public class InvalidPermissionException extends Exception {
    public InvalidPermissionException() {
        super("Invalid permission informed");
    }
}

package com.springtodo.core.identity_and_access.domain.exception;

public class OldPasswordDoesNotEqualToUserPassword extends Exception {
    public OldPasswordDoesNotEqualToUserPassword() {
        super("old password does not equal to user password");
    }
}

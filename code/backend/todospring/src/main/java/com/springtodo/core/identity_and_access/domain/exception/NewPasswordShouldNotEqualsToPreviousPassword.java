package com.springtodo.core.identity_and_access.domain.exception;

public class NewPasswordShouldNotEqualsToPreviousPassword extends Exception {
    public NewPasswordShouldNotEqualsToPreviousPassword() {

        super("passwords should not equals");
    }
}

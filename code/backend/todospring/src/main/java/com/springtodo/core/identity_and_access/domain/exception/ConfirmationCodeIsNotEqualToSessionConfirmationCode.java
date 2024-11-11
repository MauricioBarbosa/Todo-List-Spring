package com.springtodo.core.identity_and_access.domain.exception;

public class ConfirmationCodeIsNotEqualToSessionConfirmationCode extends Exception {

    public ConfirmationCodeIsNotEqualToSessionConfirmationCode(
        String informedConfirmationCode
    ) {
        super(
            "confirmation code" +
            informedConfirmationCode +
            "is not equal to session confirmation code"
        );
    }
}

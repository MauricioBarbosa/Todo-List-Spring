package com.springtodo.core.identity_and_access.domain.exception;

public class ConfirmationCodeIsNotEqualToSessionConfirmationCode extends Error {

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

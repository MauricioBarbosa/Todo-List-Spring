package com.springtodo.core.identity_and_access.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmCodeInput {

    private String confirmationCode;
    private String sessionToken;

    public ConfirmCodeInput(String confirmationCode, String sessionToken) {
        this.confirmationCode = confirmationCode;
        this.sessionToken = sessionToken;
    }
}

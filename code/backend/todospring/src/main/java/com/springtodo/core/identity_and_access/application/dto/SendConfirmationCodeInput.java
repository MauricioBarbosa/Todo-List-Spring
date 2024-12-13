package com.springtodo.core.identity_and_access.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendConfirmationCodeInput {
    private String sessionToken;

    public SendConfirmationCodeInput(String sessionToken){
        this.sessionToken = sessionToken;
    }
}

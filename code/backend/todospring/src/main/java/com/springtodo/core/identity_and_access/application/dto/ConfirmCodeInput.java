package com.springtodo.core.identity_and_access.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmCodeInput {

    private String confirmationCode;

    public ConfirmCodeInput(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }
}

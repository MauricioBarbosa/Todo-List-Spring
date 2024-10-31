package com.springtodo.core.identity_and_access.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IsSessionConfirmatedInput {

    private String sessionToken;

    public IsSessionConfirmatedInput(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}

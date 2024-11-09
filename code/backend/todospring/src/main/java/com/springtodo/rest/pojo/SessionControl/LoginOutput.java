package com.springtodo.rest.pojo.SessionControl;

import lombok.Getter;

@Getter
public class LoginOutput {

    private String sessionToken;

    public LoginOutput(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}

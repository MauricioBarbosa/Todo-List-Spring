package com.springtodo.rest.pojo.session_control;

import lombok.Getter;

@Getter
public class LoginOutput {

    private String sessionToken;

    public LoginOutput(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}

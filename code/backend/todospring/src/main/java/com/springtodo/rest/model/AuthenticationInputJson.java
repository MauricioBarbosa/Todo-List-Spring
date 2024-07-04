package com.springtodo.rest.model;

import lombok.Getter;

@Getter
public class AuthenticationInputJson {
    private String email;
    private String password;

    AuthenticationInputJson(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

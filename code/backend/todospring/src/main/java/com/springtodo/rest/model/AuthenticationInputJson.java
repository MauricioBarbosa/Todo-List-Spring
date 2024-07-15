package com.springtodo.rest.model;

import lombok.Getter;

@Getter
public class AuthenticationInputJson {
    private String email;
    private String password;
}

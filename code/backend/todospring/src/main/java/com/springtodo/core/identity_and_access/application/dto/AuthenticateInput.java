package com.springtodo.core.identity_and_access.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticateInput {
    public String email;
    public String password;
}

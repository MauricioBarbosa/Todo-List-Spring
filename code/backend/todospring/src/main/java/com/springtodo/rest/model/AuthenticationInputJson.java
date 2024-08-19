package com.springtodo.rest.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuthenticationInputJson {
    @NotBlank(message = "email not informed")
    @Email(message = "email must be an email")
    private String email;
    @NotBlank(message = "password not informed")
    private String password;
}

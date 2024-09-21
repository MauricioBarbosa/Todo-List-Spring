package com.springtodo.rest.pojo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuthenticationInputJson {
    @NotBlank(message = "email not informed")
    @Email(message = "email must be a valid email")
    String email;
    @NotBlank(message = "password must be informed")
    String password;

    public AuthenticationInputJson(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

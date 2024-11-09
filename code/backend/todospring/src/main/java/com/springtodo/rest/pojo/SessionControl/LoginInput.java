package com.springtodo.rest.pojo.SessionControl;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginInput {

    @NotBlank(message = "email not informed")
    @Email(message = "email must be a valid email")
    String email;

    @NotBlank(message = "password must be informed")
    String password;
}

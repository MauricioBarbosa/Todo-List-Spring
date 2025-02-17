package com.springtodo.rest.pojo.authenticate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticateRequestBody {

    @Email(message = "email informed is not an email")
    @NotBlank(message = "email must be informed")
    public String email;

    @NotBlank(message = "password must be informed")
    @Size(max = 20, min = 10, message = "password must have at least 10 and up to 20 characters")
    public String password;
}

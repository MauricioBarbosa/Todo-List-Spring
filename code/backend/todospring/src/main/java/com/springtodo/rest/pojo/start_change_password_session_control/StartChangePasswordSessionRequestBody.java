package com.springtodo.rest.pojo.start_change_password_session_control;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartChangePasswordSessionRequestBody {
    @NotBlank(message = "userEmail must be informed")
    @Email(message = "userEmail must be an email")
    private String userEmail;
}

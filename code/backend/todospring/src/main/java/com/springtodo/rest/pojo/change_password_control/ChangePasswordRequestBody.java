package com.springtodo.rest.pojo.change_password_control;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequestBody {
    @NotBlank(message = "oldPassword must be informed")
    String oldPassword;

    @NotBlank(message = "newPassword must be informed")
    String newPassword;
}

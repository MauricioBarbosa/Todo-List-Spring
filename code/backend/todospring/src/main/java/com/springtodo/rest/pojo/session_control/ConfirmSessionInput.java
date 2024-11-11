package com.springtodo.rest.pojo.session_control;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmSessionInput {

    @NotBlank(message = "confirmation code must be informed")
    String confirmationCode;
}

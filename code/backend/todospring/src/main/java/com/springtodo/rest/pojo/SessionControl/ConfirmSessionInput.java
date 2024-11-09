package com.springtodo.rest.pojo.SessionControl;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmSessionInput {

    @NotBlank(message = "confirmation code must be informed")
    String confirmationCode;
}

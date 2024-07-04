package com.springtodo.core.autentication.application.usecase.authenticate.dto;

import lombok.Getter;

public class InputDTO {
    private @Getter String email;
    private @Getter String password;

    InputDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

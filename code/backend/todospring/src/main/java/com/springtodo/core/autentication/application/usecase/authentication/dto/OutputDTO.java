package com.springtodo.core.autentication.application.usecase.authentication.dto;

import lombok.Getter;

public class OutputDTO {
    public @Getter String token;

    public OutputDTO(String token) {
        this.token = token;
    }
}

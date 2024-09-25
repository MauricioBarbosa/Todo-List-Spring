package com.springtodo.core.autentication.domain.value_object;

import lombok.Getter;

@Getter;
public class ConfirmationCode {
    private String code;

    ConfirmationCode(String code) {
        this.code = code;
    }

    public boolean equals(String inputCode) {
        return inputCode == this.code;
    }
}

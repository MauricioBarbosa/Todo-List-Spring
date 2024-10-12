package com.springtodo.core.identity_and_access.domain.value_object;

import java.util.UUID;
import lombok.Getter;

@Getter
public class ConfirmationCode {

    private String code;

    public ConfirmationCode(String code) {
        this.code = code;
    }

    public ConfirmationCode(int codeSize) {
        this.generateCode(codeSize);
    }

    public boolean equals(ConfirmationCode aConfirmationCode) {
        return this.code == aConfirmationCode.getCode();
    }

    private void generateCode(int codeSize) {
        this.code = UUID.randomUUID()
            .toString()
            .replace("-", "")
            .toUpperCase()
            .substring(0, codeSize);
    }

    @Override
    public String toString() {
        return "ConfirmationCode{" + "code='" + this.code + '\'' + '}';
    }
}

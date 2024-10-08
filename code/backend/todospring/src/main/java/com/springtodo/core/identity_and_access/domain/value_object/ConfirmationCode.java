package com.springtodo.core.identity_and_access.domain.value_object;

public class ConfirmationCode {

    private String code;
    private UserId userId;

    ConfirmationCode(String code, UserId userId) {
        this.code = code;
        this.userId = userId;
    }

    public boolean equals(ConfirmationCode aConfirmationCode) {
        return this.generateId() == aConfirmationCode.generateId();
    }

    public String generateId() {
        return userId.getId() + ':' + this.code;
    }
}

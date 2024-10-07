package com.springtodo.core.identity_and_access.domain.value_object;

import lombok.Getter;

@Getter
public class UserPassword {

    String password;

    public UserPassword(String password) {
        this.password = password;
    }

    public boolean equals(UserPassword aUserPassword) {
        return aUserPassword.getPassword().equals(this.password);
    }
}

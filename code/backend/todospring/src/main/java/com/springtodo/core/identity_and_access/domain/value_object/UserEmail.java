package com.springtodo.core.identity_and_access.domain.value_object;

import lombok.Getter;

@Getter
public class UserEmail {

    String email;

    public UserEmail(String email) {
        this.email = email;
    }
}

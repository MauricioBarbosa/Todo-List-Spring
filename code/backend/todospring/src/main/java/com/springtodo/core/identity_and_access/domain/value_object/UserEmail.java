package com.springtodo.core.identity_and_access.domain.value_object;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class UserEmail implements Serializable {

    String email;

    public UserEmail(String email) {
        this.email = email;
    }
}

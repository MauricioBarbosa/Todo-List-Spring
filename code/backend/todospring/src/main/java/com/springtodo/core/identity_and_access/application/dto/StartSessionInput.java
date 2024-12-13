package com.springtodo.core.identity_and_access.application.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartSessionInput implements Serializable {

    private String email;
    private String password;

    public StartSessionInput(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

package com.springtodo.core.identity_and_access.application.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartSessionOutput implements Serializable {

    private String sessionToken;

    public StartSessionOutput(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}

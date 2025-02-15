package com.springtodo.core.identity_and_access.application.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartUserSessionInput implements Serializable {

    private String email;
    private String password;

}

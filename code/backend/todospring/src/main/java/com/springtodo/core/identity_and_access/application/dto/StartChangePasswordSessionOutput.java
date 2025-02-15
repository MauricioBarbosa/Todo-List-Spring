package com.springtodo.core.identity_and_access.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartChangePasswordSessionOutput {
    private String sessionToken;
}

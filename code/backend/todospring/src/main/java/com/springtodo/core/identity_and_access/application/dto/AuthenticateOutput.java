package com.springtodo.core.identity_and_access.application.dto;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticateOutput {
    public String sessionToken;
    public Boolean isUserConfirmed;
    public Boolean confirmed;
    public ArrayList<String> permissions;
    public String expiresIn;
}

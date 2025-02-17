package com.springtodo.rest.pojo.authenticate;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticateResponse {
    public String sessionToken;
    public Boolean isUserConfirmated;
    public Boolean confirmated;
    public ArrayList<String> permissions;
    public String expiresIn;
}

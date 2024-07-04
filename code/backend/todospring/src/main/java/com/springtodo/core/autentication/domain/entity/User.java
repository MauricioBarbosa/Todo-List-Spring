package com.springtodo.core.autentication.domain.entity;

import com.springtodo.core.autentication.domain.exception.EmailNotInformed;
import com.springtodo.core.autentication.domain.exception.PasswordNotInformed;

import lombok.Getter;

@Getter
public class User {
    private String email;
    private String password;

    User(String email, String password) throws EmailNotInformed, PasswordNotInformed {
        if (email == null) {
            throw new EmailNotInformed("Email was not informed");
        }

        if (password == null) {
            throw new PasswordNotInformed("Password was not informed");
        }

        this.email = email;
        this.password = password;
    }

    User(String email) throws EmailNotInformed {
        if (email == null) {
            throw new EmailNotInformed("Email was not informed");
        }

        this.email = email;
        this.password = null;
    }

    public boolean isThisUser(String password) {
        if (password == this.password) {
            return true;
        }
        return false;
    }
}

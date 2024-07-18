package com.springtodo.core.autentication.domain.entity;

import java.io.Serializable;

import com.springtodo.core.autentication.domain.exception.EmailNotInformed;
import com.springtodo.core.autentication.domain.exception.IdNotInformed;
import com.springtodo.core.autentication.domain.exception.PasswordNotInformed;

import lombok.Getter;

@Getter
public class User implements Serializable {
    private String id;
    private String email;
    private String password;

    public User(String id, String email, String password) throws EmailNotInformed, PasswordNotInformed, IdNotInformed {
        if (id == null) {
            throw new IdNotInformed("Id was not informed");
        }

        if (email == null) {
            throw new EmailNotInformed("Email was not informed");
        }

        if (password == null) {
            throw new PasswordNotInformed("Password was not informed");
        }

        this.email = email;
        this.password = password;
    }

    public boolean isThisUser(String password) {
        if (password == this.password) {
            return true;
        }
        return false;
    }
}

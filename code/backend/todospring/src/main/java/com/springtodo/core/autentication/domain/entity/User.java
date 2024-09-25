package com.springtodo.core.autentication.domain.entity;

import com.springtodo.core.autentication.domain.exception.EmailNotInformed;
import com.springtodo.core.autentication.domain.exception.IdNotInformed;
import com.springtodo.core.autentication.domain.exception.PasswordNotInformed;
import com.springtodo.core.autentication.domain.value_object.ConfirmationCode;

import lombok.Getter;

@Getter
public class User {
    private String id;
    private String email;
    private String password;
    private ConfirmationCode confirmationCode;

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

        this.id = id;
        this.email = email;
        this.password = password;
    }

    public boolean isThisUser(String password) {
        if (this.password.equals(password)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

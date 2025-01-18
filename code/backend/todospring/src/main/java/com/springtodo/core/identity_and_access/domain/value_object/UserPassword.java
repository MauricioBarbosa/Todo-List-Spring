package com.springtodo.core.identity_and_access.domain.value_object;

import com.springtodo.core.identity_and_access.domain.exception.InvalidPassword;

import lombok.Getter;

@Getter
public class UserPassword {

    String password;

    public UserPassword(String password) {
        this.password = password;
    }

    public boolean equals(UserPassword aUserPassword) {
        return aUserPassword.getPassword().equals(this.password);
    }

    public static UserPassword create(String password) throws InvalidPassword {

        if (password.length() < 10) {
            throw new InvalidPassword("password must have at least 10 characters");
        }

        if (!password.matches(".*[a-z].*")) {
            throw new InvalidPassword("Password must contain at least one lowercase letter.");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new InvalidPassword("Password must contain at least one uppercase letter.");
        }

        if (!password.matches(".*[0-9].*")) {
            throw new InvalidPassword("Password must contain at least one number.");
        }

        if (!password.matches(".*[!@#$%^&*].*")) {
            throw new InvalidPassword("Password must contain at least one special character.");
        }

        return new UserPassword(password);
    }
}

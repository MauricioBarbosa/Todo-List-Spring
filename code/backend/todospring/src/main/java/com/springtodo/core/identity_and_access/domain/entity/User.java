package com.springtodo.core.identity_and_access.domain.entity;

import com.springtodo.core.identity_and_access.domain.exception.EmailNotInformed;
import com.springtodo.core.identity_and_access.domain.exception.IdNotInformed;
import com.springtodo.core.identity_and_access.domain.exception.PasswordNotInformed;
import com.springtodo.core.identity_and_access.domain.value_object.UserEmail;
import com.springtodo.core.identity_and_access.domain.value_object.UserId;
import com.springtodo.core.identity_and_access.domain.value_object.UserPassword;
import lombok.Getter;

@Getter
public class User {

    private UserId id;
    private UserEmail email;
    private UserPassword password;

    public User(UserId anId, UserEmail anEmail, UserPassword aPassword)
        throws EmailNotInformed, PasswordNotInformed, IdNotInformed {
        if (id == null) {
            throw new IdNotInformed("Id was not informed");
        }

        if (email == null) {
            throw new EmailNotInformed("Email was not informed");
        }

        if (password == null) {
            throw new PasswordNotInformed("Password was not informed");
        }

        this.email = anEmail;
        this.id = anId;
        this.password = aPassword;
    }

    public boolean passwordEquals(UserPassword aPassword) {
        /**
        1 - verify is password equals
        1.1 - password not equals
        1.1.1 - return false
        1.2 - password equals
        1.2.1 - return true
        */

        throw new RuntimeException("Not implemented yet");
    }
}

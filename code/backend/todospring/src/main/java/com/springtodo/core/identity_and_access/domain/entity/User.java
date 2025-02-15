package com.springtodo.core.identity_and_access.domain.entity;

import com.springtodo.core.identity_and_access.domain.exception.EmailNotInformed;
import com.springtodo.core.identity_and_access.domain.exception.IdNotInformed;
import com.springtodo.core.identity_and_access.domain.exception.NewPasswordShouldNotEqualsToPreviousPassword;
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
        if (anId == null) {
            throw new IdNotInformed("Id was not informed");
        }

        if (anEmail == null) {
            throw new EmailNotInformed("Email was not informed");
        }

        if (aPassword == null) {
            throw new PasswordNotInformed("Password was not informed");
        }

        this.email = anEmail;
        this.id = anId;
        this.password = aPassword;
    }

    public boolean passwordEquals(UserPassword aPassword) {
        return this.password.equals(aPassword);
    }

    public void changePasswordTo(UserPassword newPassword) throws NewPasswordShouldNotEqualsToPreviousPassword {
        if (passwordEquals(newPassword)) {
            throw new NewPasswordShouldNotEqualsToPreviousPassword();
        }

        this.password = newPassword;
    }

    public static User create(String email, String aPassword) {
        return new User(UserId.createNewUserId(), new UserEmail(email), new UserPassword(aPassword));
    }
}

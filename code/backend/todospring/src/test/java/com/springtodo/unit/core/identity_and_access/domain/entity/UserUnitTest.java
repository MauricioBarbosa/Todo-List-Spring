package com.springtodo.unit.core.identity_and_access.domain.entity;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.springtodo.core.identity_and_access.domain.entity.User;
import com.springtodo.core.identity_and_access.domain.exception.EmailNotInformed;
import com.springtodo.core.identity_and_access.domain.exception.IdNotInformed;
import com.springtodo.core.identity_and_access.domain.exception.PasswordNotInformed;
import com.springtodo.core.identity_and_access.domain.value_object.UserEmail;
import com.springtodo.core.identity_and_access.domain.value_object.UserId;
import com.springtodo.core.identity_and_access.domain.value_object.UserPassword;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserUnitTest {

    @Test
    @DisplayName("It should thrown IdNotInformed exception")
    void shouldThrowIdNotInformedException() {
        UserEmail userEmail = new UserEmail("someemail@gmail.com");
        UserPassword userPassword = new UserPassword("#somePassword");

        assertThrows(IdNotInformed.class, () -> {
            new User(null, userEmail, userPassword);
        });
    }

    @Test
    @DisplayName("It should thrown EmailNotInformed exception")
    void shouldThrowEmailNotInformedException() {
        UserId userId = new UserId();
        UserPassword userPassword = new UserPassword("#somePassword");

        assertThrows(EmailNotInformed.class, () -> {
            new User(userId, null, userPassword);
        });
    }

    @Test
    @DisplayName("It should thrown PasswordNotInformed exception")
    void shouldThrowPasswordNotInformedException() {
        UserId userId = new UserId();
        UserEmail userEmail = new UserEmail("someemail@gmail.com");

        assertThrows(PasswordNotInformed.class, () -> {
            new User(userId, userEmail, null);
        });
    }

    @Test
    @DisplayName("It should return that password not equals")
    void shouldReturnPasswordNotEquals() {
        UserId userId = new UserId();
        UserEmail userEmail = new UserEmail("someemail@gmail.com");
        UserPassword userPassword = new UserPassword("#somePassword");
        UserPassword anotherPassword = new UserPassword("#differentPassword");

        User user = new User(userId, userEmail, userPassword);

        assertFalse(user.passwordEquals(anotherPassword));
    }

    @Test
    @DisplayName("It should return that password equals")
    void shouldReturnPasswordEquals() {
        UserId userId = new UserId();
        UserEmail userEmail = new UserEmail("someemail@gmail.com");
        UserPassword userPassword = new UserPassword("#somePassword");
        UserPassword anotherPassword = new UserPassword("#somePassword");

        User user = new User(userId, userEmail, userPassword);

        assertTrue(user.passwordEquals(anotherPassword));
    }
}

package com.springtodo.unit.core.authentication.domain.entity;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.springtodo.core.autentication.domain.entity.User;
import com.springtodo.core.autentication.domain.exception.EmailNotInformed;
import com.springtodo.core.autentication.domain.exception.IdNotInformed;
import com.springtodo.core.autentication.domain.exception.PasswordNotInformed;

public class UserUnitTest {
    @Test
    @DisplayName("Should throw IdNotInformed when id is not informed")
    void shouldThrowIdNotInformedException() {
        String email = "someemail@dot.com";
        String password = "somepassword";
        String id = null;

        assertThrows(IdNotInformed.class, () -> {
            new User(id, email, password);
        });
    }

    @Test
    @DisplayName("Should throw EmailNotInformed when email is not informed")
    void shouldThrowIdEmailNotInformedException() {
        String email = null;
        String password = "somepassword";
        String id = "someId";

        assertThrows(EmailNotInformed.class, () -> {
            new User(id, email, password);
        });
    }

    @Test
    @DisplayName("Should throw PasswordNotInformed when password is not informed")
    void shouldThrowPasswordNotInformedException() {
        String email = "someemail@dot.com";
        String password = null;
        String id = "someId";

        assertThrows(PasswordNotInformed.class, () -> {
            new User(id, email, password);
        });
    }

    @Test
    @DisplayName("Should return false when asked if the user is authentic")
    void shouldNotBeEqual() {
        String email = "someemail@dot.com";
        String password = "somepassword";
        String id = "someId";

        String informedPassword = "InformedPassword";

        User user = new User(id, email, password);

        assertFalse(user.isThisUser(informedPassword));
    }

    @Test
    @DisplayName("Should return true when asked if the user is authentic")
    void shouldBeEqual() {
        String email = "someemail@dot.com";
        String password = "somepassword";
        String id = "someId";

        String informedPassword = "somepassword";

        User user = new User(id, email, password);

        assertTrue(user.isThisUser(informedPassword));
    }
}

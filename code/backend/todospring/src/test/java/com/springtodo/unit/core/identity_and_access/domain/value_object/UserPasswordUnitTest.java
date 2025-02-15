package com.springtodo.unit.core.identity_and_access.domain.value_object;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.springtodo.core.identity_and_access.domain.exception.InvalidPassword;
import com.springtodo.core.identity_and_access.domain.value_object.UserPassword;

public class UserPasswordUnitTest {
    @Test
    void it_shouldThrowInvalidPasswordException() {
        String weakPassword = "someweakpassword";

        assertThrows(InvalidPassword.class, () -> {
            UserPassword.create(weakPassword);
        });
    }

    @Test
    void it_shouldReturnAValidUserPassword() {
        String strongPassword = "Abc123@XYZ!";

        assertDoesNotThrow(() -> {
            UserPassword userPassword = UserPassword.create(strongPassword);

            assertTrue(userPassword.getPassword().equals(strongPassword));
        });
    }
}

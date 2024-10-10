package com.springtodo.unit.core.identity_and_access.domain.value_object;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.springtodo.core.identity_and_access.domain.value_object.ConfirmationCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ConfirmationCodeUnitTest {

    @Test
    @DisplayName("It should return auto generated code")
    void shouldReturnAutoGeneratedCode() {
        final int codeSize = 5;
        final ConfirmationCode confirmationCode = new ConfirmationCode(
            codeSize
        );

        assertEquals(confirmationCode.getCode().getClass(), String.class);
        assertEquals(confirmationCode.getCode().length(), codeSize);
    }

    @Test
    @DisplayName("It should return ConfirmationCode with code from outside")
    void shouldReturnCodeFromOutside() {
        final String confirmationCodeString = "ASDR4";

        final ConfirmationCode confirmationCode = new ConfirmationCode(
            confirmationCodeString
        );

        assertEquals(confirmationCode.getCode(), confirmationCodeString);
    }

    @Test
    @DisplayName("It should assert codes not equals")
    void shouldAssertCodesNotEquals() {
        final ConfirmationCode firstConfirmationCode = new ConfirmationCode(
            "ASDR4"
        );
        final ConfirmationCode secondConfirmationCode = new ConfirmationCode(
            "DFGTH"
        );

        assertFalse(firstConfirmationCode.equals(secondConfirmationCode));
    }

    @Test
    @DisplayName("It should assert codes equals")
    void shouldAssertCodesEquals() {
        final ConfirmationCode firstConfirmationCode = new ConfirmationCode(
            "ASDR4"
        );
        final ConfirmationCode secondConfirmationCode = new ConfirmationCode(
            "ASDR4"
        );

        assertTrue(firstConfirmationCode.equals(secondConfirmationCode));
    }
}

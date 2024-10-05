package com.springtodo.unit.core.identity_and_access.domain.value_object;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.springtodo.core.identity_and_access.domain.value_object.UserId;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserIdUnitTest {

    @Test
    @DisplayName("It should return a userId with informed id")
    void shouldReturnUserIdWithInformedId() {
        String id = UUID.randomUUID().toString();
        UserId userId = new UserId(id);

        assertEquals(id, userId.getId());
    }

    @Test
    @DisplayName("It should return a userId with generated id")
    void shouldReturnUserIdWithGeneratedId() {
        UserId userId = new UserId();

        assertEquals(userId.getId().getClass(), String.class);
    }
}

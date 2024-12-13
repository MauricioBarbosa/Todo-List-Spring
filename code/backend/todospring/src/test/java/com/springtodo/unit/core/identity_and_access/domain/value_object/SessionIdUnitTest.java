package com.springtodo.unit.core.identity_and_access.domain.value_object;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.springtodo.core.identity_and_access.domain.value_object.SessionId;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SessionIdUnitTest {

    @Test
    @DisplayName("It should return a SessionId with informed id")
    void shouldReturnSessionIdWithInformedId() {
        String id = UUID.randomUUID().toString();
        SessionId sessionId = new SessionId(id);

        assertEquals(id, sessionId.getId());
    }

    @Test
    @DisplayName("It should return a SessionId with generated id")
    void shouldReturnSessionIdWithGeneratedId() {
        SessionId sessionId = new SessionId();

        assertEquals(sessionId.getId().getClass(), String.class);
    }
}

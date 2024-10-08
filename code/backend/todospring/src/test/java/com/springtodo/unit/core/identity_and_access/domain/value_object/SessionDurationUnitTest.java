package com.springtodo.unit.core.identity_and_access.domain.value_object;

import com.springtodo.core.identity_and_access.domain.value_object.SessionDuration;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SessionDurationUnitTest {

    @Test
    @DisplayName("It should return session duration of 3 minutes")
    void shouldReturnSessionDurationInMinutes() {
        Long threeMinutesInSeconds = Long.valueOf(60 * 3);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now()
            .plusSeconds(threeMinutesInSeconds);

        SessionDuration sessionDuration = new SessionDuration(
            threeMinutesInSeconds
        );
    }
}

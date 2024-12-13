package com.springtodo.unit.core.identity_and_access.domain.value_object;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        int durationInMinutes = end.getMinute() - start.getMinute();

        SessionDuration sessionDuration = new SessionDuration(
            threeMinutesInSeconds
        );

        assertEquals(sessionDuration.getDurationInMinutes(), durationInMinutes);
    }

    @Test
    @DisplayName("It should return session duration of 180 seconds")
    void shouldReturnSessionDurationInSeconds() {
        Long threeMinutesInSeconds = Long.valueOf(60 * 3);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now()
            .plusSeconds(threeMinutesInSeconds);

        int durationInSeconds = end.getSecond() - start.getSecond();

        SessionDuration sessionDuration = new SessionDuration(
            threeMinutesInSeconds
        );

        assertEquals(sessionDuration.getDurationInSeconds(), durationInSeconds);
    }
}

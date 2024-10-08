package com.springtodo.core.identity_and_access.domain.value_object;

import com.springtodo.core.identity_and_access.domain.value_object.SessionDuration;
import java.time.LocalDateTime;

public class SessionDuration {

    private LocalDateTime start;
    private LocalDateTime end;

    public SessionDuration(Long durationInSeconds) {
        this.start = LocalDateTime.now();
        this.end = LocalDateTime.now().plusSeconds(durationInSeconds);
    }

    public int getDurationInSeconds() {
        return this.end.getSecond() - this.start.getSecond();
    }

    public int getDurationInMinutes() {
        return this.end.getMinute() - this.start.getMinute();
    }
}

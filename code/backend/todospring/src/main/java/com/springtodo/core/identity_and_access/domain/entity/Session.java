package com.springtodo.core.identity_and_access.domain.entity;

import com.springtodo.core.identity_and_access.domain.exception.ConfirmationCodeIsNotEqualToSessionConfirmationCode;
import com.springtodo.core.identity_and_access.domain.value_object.ConfirmationCode;
import com.springtodo.core.identity_and_access.domain.value_object.SessionDuration;
import com.springtodo.core.identity_and_access.domain.value_object.SessionId;
import com.springtodo.core.identity_and_access.domain.value_object.SessionStatus;
import com.springtodo.core.identity_and_access.domain.value_object.SessionStatusConfirmated;
import com.springtodo.core.identity_and_access.domain.value_object.SessionStatusStarted;
import com.springtodo.core.identity_and_access.domain.value_object.UserId;
import lombok.Getter;

@Getter
public class Session {

    private SessionId sessionId;
    private UserId userId;
    private SessionDuration duration;
    private SessionStatus status;
    private ConfirmationCode confirmationCode;

    public Session(
        User user,
        Long durationInSeconds,
        int confirmationCodeSize
    ) {
        this.userId = user.getId();
        this.duration = new SessionDuration(durationInSeconds);

        this.sessionId = new SessionId();
        this.status = new SessionStatusStarted();
        this.confirmationCode = new ConfirmationCode(confirmationCodeSize);
    }

    public Session(
        SessionId sessionId,
        UserId userId,
        SessionDuration duration,
        SessionStatus status,
        ConfirmationCode confirmationCode
    ) {
        this.confirmationCode = confirmationCode;
        this.userId = userId;
        this.duration = duration;
        this.status = status;
        this.sessionId = sessionId;
    }

    public boolean isConfirmated() {
        return this.status.getClass() == SessionStatusConfirmated.class;
    }

    public void confirm(ConfirmationCode aConfirmationCode)
        throws ConfirmationCodeIsNotEqualToSessionConfirmationCode {
        if (!this.confirmationCode.equals(aConfirmationCode)) {
            throw new ConfirmationCodeIsNotEqualToSessionConfirmationCode(
                aConfirmationCode.getCode()
            );
        }

        this.status = new SessionStatusConfirmated();
    }
}

package com.springtodo.unit.core.identity_and_access.domain.builder;

import com.springtodo.core.identity_and_access.domain.entity.Session;
import com.springtodo.core.identity_and_access.domain.value_object.ConfirmationCode;
import com.springtodo.core.identity_and_access.domain.value_object.SessionDuration;
import com.springtodo.core.identity_and_access.domain.value_object.SessionId;
import com.springtodo.core.identity_and_access.domain.value_object.SessionStatus;
import com.springtodo.core.identity_and_access.domain.value_object.SessionStatusStarted;
import com.springtodo.core.identity_and_access.domain.value_object.UserId;

public class SessionBuilder {

    private SessionId sessionId = new SessionId("#someSessionId");
    private UserId userId = new UserId("#someUserId");
    private SessionDuration sessionDuration = new SessionDuration(Long.valueOf(10));
    private SessionStatus sessionStatus = new SessionStatusStarted();
    private ConfirmationCode confirmationCode = new ConfirmationCode(5);

    public Session build() {
        return new Session(sessionId, userId, sessionDuration, sessionStatus, confirmationCode);
    }
}

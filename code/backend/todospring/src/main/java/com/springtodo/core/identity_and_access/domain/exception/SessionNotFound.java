package com.springtodo.core.identity_and_access.domain.exception;

import com.springtodo.core.identity_and_access.domain.value_object.SessionId;

public class SessionNotFound extends Exception {

    public SessionNotFound(SessionId sessionId) {
        super("Session not found");
    }
}

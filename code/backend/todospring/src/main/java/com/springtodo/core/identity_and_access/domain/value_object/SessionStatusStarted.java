package com.springtodo.core.identity_and_access.domain.value_object;

public class SessionStatusStarted extends SessionStatus {

    public SessionStatusStarted() {
        super();
    }

    @Override
    public String toString() {
        return "SessionStatus = { CONFIRMATED }";
    }
}

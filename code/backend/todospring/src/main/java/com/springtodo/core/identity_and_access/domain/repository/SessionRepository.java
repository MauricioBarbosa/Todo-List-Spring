package com.springtodo.core.identity_and_access.domain.repository;

import com.springtodo.core.identity_and_access.domain.entity.Session;

public abstract class SessionRepository {

    public void save(Session aSession) {
        throw new RuntimeException("Not implemented yet");
    }
}

package com.springtodo.core.identity_and_access.domain.repository;

import com.springtodo.core.identity_and_access.domain.entity.Session;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSaveSession;

public abstract class SessionRepository {

    public abstract void save(Session aSession) throws CouldNotSaveSession;
}

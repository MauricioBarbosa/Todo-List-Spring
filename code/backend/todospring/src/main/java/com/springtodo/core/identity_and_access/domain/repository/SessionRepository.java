package com.springtodo.core.identity_and_access.domain.repository;

import com.springtodo.core.identity_and_access.domain.entity.Session;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotFindSession;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSaveSession;
import com.springtodo.core.identity_and_access.domain.exception.SessionNotFound;
import com.springtodo.core.identity_and_access.domain.value_object.SessionId;

public abstract class SessionRepository {

    public abstract void save(Session aSession) throws CouldNotSaveSession;

    public abstract Session get(SessionId sessionId)
        throws SessionNotFound, CouldNotFindSession;
}

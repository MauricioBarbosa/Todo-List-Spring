package com.springtodo.core.identity_and_access.infrastructure.persistence.repository.in_memory;

import java.util.HashMap;
import java.util.Map;

import com.springtodo.core.identity_and_access.domain.entity.Session;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotFindSession;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSaveSession;
import com.springtodo.core.identity_and_access.domain.exception.SessionNotFound;
import com.springtodo.core.identity_and_access.domain.repository.SessionRepository;
import com.springtodo.core.identity_and_access.domain.value_object.SessionId;

public class InMemorySessionRepository extends SessionRepository {
    private final Map<SessionId, Session> sessionStore = new HashMap();

    @Override
    public Session get(SessionId sessionId) throws SessionNotFound, CouldNotFindSession {
        try {
            Session session = sessionStore.get(sessionId);
            if (session == null) {
                throw new SessionNotFound(sessionId);
            }

            return session;
        } catch (Exception e) {
            throw new CouldNotFindSession(e.getMessage());
        }
    }

    @Override
    public void save(Session aSession) throws CouldNotSaveSession {
        try {
            sessionStore.put(aSession.getSessionId(), aSession);
        } catch (Exception e) {
            throw new CouldNotSaveSession(e.getMessage());
        }
    }
}

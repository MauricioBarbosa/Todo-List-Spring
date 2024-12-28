package com.springtodo.core.identity_and_access.infrastructure.persistence.repository.in_memory;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;

import com.springtodo.core.identity_and_access.domain.entity.Session;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotFindSession;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSaveSession;
import com.springtodo.core.identity_and_access.domain.exception.SessionNotFound;
import com.springtodo.core.identity_and_access.domain.repository.SessionRepository;
import com.springtodo.core.identity_and_access.domain.value_object.SessionId;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InMemorySessionRepository extends SessionRepository {
    private final Map<String, Session> sessionStore = new HashMap();

    @Override
    @Cacheable("session")
    public Session get(SessionId sessionId) throws SessionNotFound, CouldNotFindSession {
        try {

            log.info("recovering session, {}, {}", sessionId, sessionStore);

            Session session = sessionStore.get(sessionId.getId());

            if (session == null) {
                log.warn("session don't exist, {}, {}", sessionId, sessionStore);

                throw new SessionNotFound(sessionId);
            }

            return session;
        } catch (SessionNotFound e) {
            throw e;
        } catch (Exception e) {
            log.warn("could not find session", sessionId, e);

            throw new CouldNotFindSession(e.getMessage());
        }
    }

    @Override
    public void save(Session aSession) throws CouldNotSaveSession {
        try {
            log.info("saving session, {} {}", aSession, sessionStore);

            sessionStore.put(aSession.getSessionId().getId(), aSession);

            log.info("session save! {} {}", aSession, sessionStore);
        } catch (Exception e) {
            log.warn("could not save session {} {}", aSession, e);

            throw new CouldNotSaveSession(e.getMessage());
        }
    }
}

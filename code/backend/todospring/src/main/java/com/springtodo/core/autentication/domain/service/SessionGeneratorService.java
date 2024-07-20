package com.springtodo.core.autentication.domain.service;

import com.springtodo.core.autentication.domain.exception.CouldNotCreateSession;

public abstract class SessionGeneratorService {
    public abstract String createSession(String userId, String email, String exp) throws CouldNotCreateSession;
}

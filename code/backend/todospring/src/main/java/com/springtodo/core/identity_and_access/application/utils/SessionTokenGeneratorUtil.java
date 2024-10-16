package com.springtodo.core.identity_and_access.application.utils;

import java.time.LocalDateTime;

import com.springtodo.core.identity_and_access.application.exception.CouldNotGenerateToken;

public abstract class SessionTokenGeneratorUtil {

    protected SessionTokenGeneratorUtil() {}

    public abstract String generate(
        String sessionId,
        LocalDateTime start,
        LocalDateTime end
    ) throws CouldNotGenerateToken;
}

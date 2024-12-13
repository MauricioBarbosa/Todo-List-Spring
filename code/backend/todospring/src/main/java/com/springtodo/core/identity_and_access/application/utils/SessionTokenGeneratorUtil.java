package com.springtodo.core.identity_and_access.application.utils;

import java.time.LocalDateTime;

import com.springtodo.core.identity_and_access.application.exception.CouldNotDecodeToken;
import com.springtodo.core.identity_and_access.application.exception.CouldNotGenerateToken;
import com.springtodo.core.identity_and_access.application.exception.InvalidToken;
import com.springtodo.core.identity_and_access.domain.value_object.SessionId;

public abstract class SessionTokenGeneratorUtil {

    protected SessionTokenGeneratorUtil() {}

    public abstract String generate(
        String sessionId,
        LocalDateTime start,
        LocalDateTime end
    ) throws CouldNotGenerateToken;

    public abstract SessionId decode(String sessionToken) throws InvalidToken, CouldNotDecodeToken;
}

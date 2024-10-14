package com.springtodo.core.identity_and_access.application.utils;

import com.springtodo.core.identity_and_access.application.exception.CouldNotGenerateToken;

public abstract class SessionTokenGeneratorUtil {

    protected SessionTokenGeneratorUtil() {}
    
    public abstract String generate(String sessionId)
        throws CouldNotGenerateToken;
}

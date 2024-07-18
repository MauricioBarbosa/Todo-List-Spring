package com.springtodo.core.autentication.infrastructure.tokengenerator;

public abstract class TokenGenerator {
    protected TokenGenerator() {
    }

    public abstract String generateToken(String userId, String email, String exp);
}

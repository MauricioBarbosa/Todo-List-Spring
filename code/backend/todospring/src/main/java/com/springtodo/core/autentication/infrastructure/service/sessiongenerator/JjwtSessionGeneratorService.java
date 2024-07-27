package com.springtodo.core.autentication.infrastructure.service.sessiongenerator;

import org.springframework.stereotype.Service;

import com.springtodo.core.autentication.domain.service.SessionGeneratorService;

@Service
public class JjwtSessionGeneratorService extends SessionGeneratorService {

    private String secretKey;
    private String applicationTimeZone;
    private String sessionIssuer;
    private String sessionSubject;

    public JjwtSessionGeneratorService(String secretKey, String applicationTimeZone, String sessionIssuer,
            String sessionSubject) {
        this.secretKey = secretKey;
        this.applicationTimeZone = applicationTimeZone;
        this.sessionIssuer = sessionIssuer;
        this.sessionSubject = sessionSubject;
    }

    @Override
    public String createSession(String userId, String email, long expirationInMinutes) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSession'");
    }

}

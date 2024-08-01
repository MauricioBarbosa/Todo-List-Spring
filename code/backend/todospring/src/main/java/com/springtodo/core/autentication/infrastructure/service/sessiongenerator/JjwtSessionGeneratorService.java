package com.springtodo.core.autentication.infrastructure.service.sessiongenerator;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.springtodo.core.autentication.domain.exception.CouldNotCreateSession;
import com.springtodo.core.autentication.domain.service.SessionGeneratorService;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;

@Service
public class JjwtSessionGeneratorService extends SessionGeneratorService {

    private String secretKey;
    private String applicationTimeZone;
    private String sessionIssuer;
    private String sessionSubject;

    private static final Logger LOG = LoggerFactory.getLogger(JjwtSessionGeneratorService.class);

    public JjwtSessionGeneratorService(String secretKey, String applicationTimeZone, String sessionIssuer,
            String sessionSubject) {
        this.secretKey = secretKey;
        this.applicationTimeZone = applicationTimeZone;
        this.sessionIssuer = sessionIssuer;
        this.sessionSubject = sessionSubject;
    }

    @Override
    public String createSession(String userId, String email, long expirationInMinutes) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(this.secretKey.getBytes());

            // Date currentDate =
            // Date.from(ZonedDateTime.now(ZoneId.of(this.applicationTimeZone)).toInstant());
            // Date expirationDate = Date
            // .from(ZonedDateTime.now(ZoneId.of(this.applicationTimeZone)).plusMinutes(expirationInMinutes)
            // .toInstant());

            return "not implemented";
        } catch (WeakKeyException e) {
            System.out.println("DEU PAU");
            LOG.error("An error has occured on generating session token", e);
            throw new CouldNotCreateSession(e.getMessage());
        }
    }

}

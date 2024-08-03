package com.springtodo.core.autentication.infrastructure.service.sessiongenerator;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.springtodo.core.autentication.domain.exception.CouldNotCreateSession;
import com.springtodo.core.autentication.domain.service.SessionGeneratorService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;

@Service
public class JjwtSessionGeneratorService extends SessionGeneratorService {

    private String secretKey;
    private String applicationTimeZone;
    private String sessionIssuer;
    private String sessionSubject;

    private Logger LOG;

    public JjwtSessionGeneratorService(String secretKey, String applicationTimeZone, String sessionIssuer,
            String sessionSubject) {
        this.secretKey = secretKey;
        this.applicationTimeZone = applicationTimeZone;
        this.sessionIssuer = sessionIssuer;
        this.sessionSubject = sessionSubject;
        this.LOG = LoggerFactory.getLogger(JjwtSessionGeneratorService.class);
    }

    @Override
    public String createSession(String userId, String email, long expirationInMinutes) {
        Map<String, String> logPayload = new HashMap<>();

        try {
            logPayload.put("secretKey", secretKey);
            logPayload.put("sessionIssuer", sessionIssuer);
            logPayload.put("sessionSubject", sessionSubject);
            logPayload.put("userId", userId);
            logPayload.put("userEmail", email);
            logPayload.put("expirationInMinutes", Long.toString(expirationInMinutes));

            SecretKey key = Keys.hmacShaKeyFor(this.secretKey.getBytes());

            Date currentDate = Date.from(ZonedDateTime.now(ZoneId.of(this.applicationTimeZone)).toInstant());
            Date expirationDate = Date
                    .from(ZonedDateTime.now(ZoneId.of(this.applicationTimeZone)).plusMinutes(expirationInMinutes)
                            .toInstant());

            System.out.println(this.applicationTimeZone);

            String jwt = Jwts.builder().issuer(this.sessionIssuer).subject(this.sessionSubject).issuedAt(currentDate)
                    .expiration(expirationDate)
                    .signWith(key)
                    .compact();

            logPayload.put("jwtSessionToken", jwt);

            LOG.info("JWT Generated!", logPayload);

            return jwt;
        } catch (WeakKeyException e) {
            LOG.error("An error has occured on generating session token", e, logPayload);
            throw new CouldNotCreateSession(e.getMessage());
        } catch (DateTimeException e) {
            LOG.error("An error has occured on getting date", e, logPayload);
            throw new CouldNotCreateSession(e.getMessage());
        }
    }

}

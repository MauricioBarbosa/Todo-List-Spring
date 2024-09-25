package com.springtodo.core.autentication.infrastructure.service.sessiongenerator;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.springtodo.core.autentication.domain.enums.AuthenticationStates;
import com.springtodo.core.autentication.domain.exception.CouldNotCreateSession;
import com.springtodo.core.autentication.domain.service.SessionGeneratorService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JjwtSessionGeneratorService extends SessionGeneratorService {

    @Value("${jwtSecretKey}")
    private String secretKey;

    @Value("${applicationTimeZone}")
    private String applicationTimeZone;

    @Value("${jwtSessionIssuer}")
    private String sessionIssuer;

    @Value("${jwtSessionSubject}")
    private String sessionSubject;

    @Override
    public String createSession(String userId, String email, long expirationInMinutes,
            AuthenticationStates authenticationState) {
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
                    .claim("userEmail", email)
                    .claim("userId", userId)
                    .claim("state", authenticationState)
                    .compact();

            logPayload.put("jwtSessionToken", jwt);

            log.info("JWT Generated!", logPayload);

            return jwt;
        } catch (WeakKeyException e) {
            log.error("An error has occured on generating session token", e, logPayload);
            throw new CouldNotCreateSession(e.getMessage());
        } catch (DateTimeException e) {
            log.error("An error has occured on getting date", e, logPayload);
            throw new CouldNotCreateSession(e.getMessage());
        }
    }

}

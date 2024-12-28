package com.springtodo.core.identity_and_access.infrastructure.utils;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.springtodo.core.identity_and_access.application.exception.CouldNotDecodeToken;
import com.springtodo.core.identity_and_access.application.exception.CouldNotGenerateToken;
import com.springtodo.core.identity_and_access.application.exception.InvalidToken;
import com.springtodo.core.identity_and_access.application.utils.SessionTokenGeneratorUtil;
import com.springtodo.core.identity_and_access.domain.value_object.SessionId;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JjwtSessionTokenGeneratorUtil extends SessionTokenGeneratorUtil {

    @Value("${jwtSecretKey}")
    private String secretKey;

    @Value("${applicationTimeZone}")
    private String applicationTimeZone;

    @Value("${jwtSessionIssuer}")
    private String sessionIssuer;

    @Value("${jwtSessionSubject}")
    private String sessionSubject;

    @Override
    public String generate(
            String sessionId,
            LocalDateTime start,
            LocalDateTime end) throws CouldNotGenerateToken {
        try {
            log.info("recovering hmacShaKeyFor {}", secretKey);

            SecretKey key = Keys.hmacShaKeyFor(this.secretKey.getBytes());

            log.info("generating date sessions for {}, {}", start, end);

            Date startDate = Date.from(
                    start.atZone(ZoneId.of(this.applicationTimeZone)).toInstant());

            Date endDate = Date.from(
                    end.atZone(ZoneId.of(this.applicationTimeZone)).toInstant());

            log.info("Generating jwt token og session id {}", sessionId);

            String jwt = Jwts.builder()
                    .issuer(this.sessionIssuer)
                    .subject(this.sessionSubject)
                    .issuedAt(startDate)
                    .expiration(endDate)
                    .signWith(key)
                    .claim("sessionId", sessionId)
                    .compact();

            log.info("jwt token generated! jwt {}, startDate: {}, endDate {}", jwt, start, end);

            return jwt;
        } catch (WeakKeyException e) {
            log.error(
                    "Could not generate session token for session id {}, error {}",
                    sessionId,
                    e);

            throw new CouldNotGenerateToken(e.getMessage());
        } catch (DateTimeException e) {
            log.error(
                    "Could not generate session token for session id {}, error {}",
                    sessionId,
                    e);

            throw new CouldNotGenerateToken(e.getMessage());
        }
    }

    @Override
    public SessionId decode(String sessionToken) throws InvalidToken, CouldNotDecodeToken {

        try {
            log.info("decoding session token, {}", sessionToken);

            SecretKey key = Keys.hmacShaKeyFor(this.secretKey.getBytes());

            String sessionId = Jwts.parser().verifyWith(key).build().parseSignedClaims(sessionToken).getPayload()
                    .get("sessionId", String.class);

            log.info("session decoded", sessionToken, sessionId);

            return new SessionId(sessionId);
        } catch (JwtException e) {
            log.warn("invalid jwt", e);

            throw new InvalidToken();
        } catch (Exception e) {
            log.error("an error has ocurred", e);

            throw new CouldNotDecodeToken(sessionToken);
        }

    }
}

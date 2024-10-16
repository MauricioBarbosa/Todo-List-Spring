package com.springtodo.core.identity_and_access.infrastructure.utils;

import com.springtodo.core.identity_and_access.application.exception.CouldNotGenerateToken;
import com.springtodo.core.identity_and_access.application.utils.SessionTokenGeneratorUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    @Value("${sessionDurationInSeconds}")
    private Long sessionDurationInSeconds;

    @Override
    public String generate(
        String sessionId,
        LocalDateTime start,
        LocalDateTime end
    ) throws CouldNotGenerateToken {
        try {
            SecretKey key = Keys.hmacShaKeyFor(this.secretKey.getBytes());

            Date startDate = Date.from(
                start.atZone(ZoneId.of(this.applicationTimeZone)).toInstant()
            );

            Date endDate = Date.from(
                end.atZone(ZoneId.of(this.applicationTimeZone)).toInstant()
            );

            String jwt = Jwts.builder()
                .issuer(this.sessionIssuer)
                .subject(this.sessionSubject)
                .issuedAt(startDate)
                .expiration(endDate)
                .signWith(key)
                .claim("sessionId", sessionId)
                .compact();

            log.info("jwt token generated! {}", jwt);

            return jwt;
        } catch (WeakKeyException e) {
            log.error(
                "error on generating jwt token, error: {}, sessionId {}, start {}, end {}",
                e,
                sessionId,
                start,
                end
            );

            throw new CouldNotGenerateToken(e.getMessage());
        } catch (DateTimeException e) {
            log.error(
                "error on generating jwt token, error: {}, sessionId {}, start {}, end {}",
                e,
                sessionId,
                start,
                end
            );
            throw new CouldNotGenerateToken(e.getMessage());
        }
    }
}

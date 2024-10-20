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

    @Override
    public String generate(
        String sessionId,
        LocalDateTime start,
        LocalDateTime end
    ) throws CouldNotGenerateToken {
        try {
            log.info("recovering hmacShaKeyFor {}", secretKey);

            SecretKey key = Keys.hmacShaKeyFor(this.secretKey.getBytes());

            log.info("generating date sessions for {}, {}", start, end);

            Date startDate = Date.from(
                start.atZone(ZoneId.of(this.applicationTimeZone)).toInstant()
            );

            Date endDate = Date.from(
                end.atZone(ZoneId.of(this.applicationTimeZone)).toInstant()
            );

            log.info("Generating jwt token og session id" + sessionId);

            String jwt = Jwts.builder()
                .issuer(this.sessionIssuer)
                .subject(this.sessionSubject)
                .issuedAt(startDate)
                .expiration(endDate)
                .signWith(key)
                .claim("sessionId", sessionId)
                .compact();

            return jwt;
        } catch (WeakKeyException e) {
            log.error(
                "Could not generate session token for session id {}, error {}",
                sessionId,
                e
            );

            throw new CouldNotGenerateToken(e.getMessage());
        } catch (DateTimeException e) {
            log.error(
                "Could not generate session token for session id {}, error {}",
                sessionId,
                e
            );

            throw new CouldNotGenerateToken(e.getMessage());
        }
    }
}

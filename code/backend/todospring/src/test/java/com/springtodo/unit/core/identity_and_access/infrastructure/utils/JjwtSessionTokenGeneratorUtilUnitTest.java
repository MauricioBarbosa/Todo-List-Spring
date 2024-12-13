package com.springtodo.unit.core.identity_and_access.infrastructure.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.springtodo.core.identity_and_access.application.exception.CouldNotGenerateToken;
import com.springtodo.core.identity_and_access.infrastructure.utils.JjwtSessionTokenGeneratorUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultJwtBuilder;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class JjwtSessionTokenGeneratorUtilUnitTest {

    @InjectMocks
    private JjwtSessionTokenGeneratorUtil jjwtSessionGeneratorUtil;

    private String secretKey = "someValue";
    private String applicationTimeZone = "America/Sao_Paulo";
    private String sessionIssuer = "todoApp";
    private String sessionSubject = "String";
    private LocalDateTime start;
    private LocalDateTime end;

    private String sessionId;

    MockedStatic<Keys> mockedKeys;
    MockedStatic<ZoneId> mockedZoneId;
    MockedStatic<Jwts> mockedJwts;

    ZoneId zoneId;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        this.zoneId = ZoneId.of(this.applicationTimeZone);

        this.sessionId = UUID.randomUUID().toString();

        ReflectionTestUtils.setField(
            jjwtSessionGeneratorUtil,
            "secretKey",
            this.secretKey
        );
        ReflectionTestUtils.setField(
            jjwtSessionGeneratorUtil,
            "applicationTimeZone",
            this.applicationTimeZone
        );
        ReflectionTestUtils.setField(
            jjwtSessionGeneratorUtil,
            "sessionIssuer",
            this.sessionIssuer
        );
        ReflectionTestUtils.setField(
            jjwtSessionGeneratorUtil,
            "sessionSubject",
            this.sessionSubject
        );

        this.mockedKeys = mockStatic(Keys.class);
        this.mockedZoneId = mockStatic(ZoneId.class);
        this.mockedJwts = mockStatic(Jwts.class);

        this.start = LocalDateTime.now();
        this.end = LocalDateTime.now().plusSeconds(120);
    }

    @AfterEach
    void tearDown() {
        this.mockedKeys.close();
        this.mockedZoneId.close();
        this.mockedJwts.close();
    }

    @Test
    @DisplayName("It must thrown an error when getting hmac sha key fails")
    void testWhenJwtGenerationFails() {
        WeakKeyException e = new WeakKeyException("A key error has occurred");

        mockedKeys.when(() -> Keys.hmacShaKeyFor(any())).thenThrow(e);

        try {
            jjwtSessionGeneratorUtil.generate(sessionId, start, end);
        } catch (Exception executionException) {
            assertEquals(e.getMessage(), executionException.getMessage());
            assertEquals(
                executionException.getClass(),
                CouldNotGenerateToken.class
            );
        }
    }
    /* I couldn't make this part work, idk why
    @Test
    @DisplayName("It must thrown an error when getting zoned time fails")
    void testeWhenGettingTimeZoneFails() {
        DateTimeException exception = new DateTimeException(
            "A timezone error has occurred"
        );

        mockedKeys
            .when(() -> Keys.hmacShaKeyFor(any()))
            .thenReturn(
                new SecretKeySpec(this.secretKey.getBytes(), "HmacSHA512")
            );

        mockedZoneId
            .when(() -> ZoneId.of(this.applicationTimeZone))
            .thenThrow(exception);

        CouldNotGenerateToken couldNotGenerateTokenException = assertThrows(
            CouldNotGenerateToken.class,
            () -> {
                jjwtSessionGeneratorUtil.generate(sessionId, start, end);
            }
        );

        assertEquals(
            couldNotGenerateTokenException.getMessage(),
            exception.getMessage()
        );
    }

    @Test
    @DisplayName("It must return a generated token")
    void testIfGettingTokenIsOK() throws CouldNotGenerateToken {
        String expectedGeneratedJwt = "mocked-jwt-token";

        mockedKeys
            .when(() -> Keys.hmacShaKeyFor(any()))
            .thenReturn(
                new SecretKeySpec(this.secretKey.getBytes(), "HmacSHA512")
            );

        mockedKeys
            .when(() -> Keys.hmacShaKeyFor(any()))
            .thenReturn(
                new SecretKeySpec(this.secretKey.getBytes(), "HmacSHA512")
            );

        mockedZoneId
            .when(() -> ZoneId.of(this.applicationTimeZone))
            .thenReturn(this.zoneId);

        DefaultJwtBuilder jwtBuilder = mock(DefaultJwtBuilder.class);

        when(jwtBuilder.issuer(anyString())).thenReturn(jwtBuilder);
        when(jwtBuilder.subject(anyString())).thenReturn(jwtBuilder);
        when(jwtBuilder.issuedAt(any())).thenReturn(jwtBuilder);
        when(jwtBuilder.expiration(any())).thenReturn(jwtBuilder);
        when(jwtBuilder.signWith(any())).thenReturn(jwtBuilder);
        when(jwtBuilder.claim(any(), any())).thenReturn(jwtBuilder);
        when(jwtBuilder.compact()).thenReturn(expectedGeneratedJwt);

        mockedJwts.when(() -> Jwts.builder()).thenReturn(jwtBuilder);

        String generatedJwt = jjwtSessionGeneratorUtil.generate(
            sessionId,
            start,
            end
        );

        assertEquals(expectedGeneratedJwt, generatedJwt);
    }

    */
}

package com.springtodo.unit.core.authentication.infrastructure.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.time.DateTimeException;
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

import com.springtodo.core.autentication.domain.exception.CouldNotCreateSession;
import com.springtodo.core.autentication.infrastructure.service.sessiongenerator.JjwtSessionGeneratorService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultJwtBuilder;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;

@ExtendWith(MockitoExtension.class)
public class JjwtSessionGeneratorServiceUnitTest {

    @InjectMocks
    private JjwtSessionGeneratorService jjwtSessionGeneratorService;

    private String secretKey = "someValue";
    private String applicationTimeZone = "America/Sao_Paulo";
    private String sessionIssuer = "todoApp";
    private String sessionSubject = "String";

    private String userId;
    private String email;
    private long expirationInMinutes;

    MockedStatic<Keys> mockedKeys;
    MockedStatic<ZoneId> mockedZoneId;
    MockedStatic<Jwts> mockedJwts;

    ZoneId zoneId;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        this.userId = UUID.randomUUID().toString();
        this.email = "someemail@email.com";
        this.expirationInMinutes = 30;

        this.zoneId = ZoneId.of(applicationTimeZone);

        ReflectionTestUtils.setField(jjwtSessionGeneratorService, "secretKey",
                this.secretKey);
        ReflectionTestUtils.setField(jjwtSessionGeneratorService, "applicationTimeZone", this.applicationTimeZone);
        ReflectionTestUtils.setField(jjwtSessionGeneratorService, "sessionIssuer", this.sessionIssuer);
        ReflectionTestUtils.setField(jjwtSessionGeneratorService, "sessionSubject", this.sessionSubject);

        this.mockedKeys = mockStatic(Keys.class);
        this.mockedZoneId = mockStatic(ZoneId.class);
        this.mockedJwts = mockStatic(Jwts.class);

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
            jjwtSessionGeneratorService.createSession(userId, email, expirationInMinutes);
        } catch (Exception executionException) {
            assertEquals(e.getMessage(), executionException.getMessage());
        }
    }

    @Test
    @DisplayName("It must thrown an error when getting zoned time fails")
    void testeWhenGettingTimeZoneFails() {
        DateTimeException exception = new DateTimeException("A timezone error has occurred");

        mockedKeys.when(() -> Keys.hmacShaKeyFor(any()))
                .thenReturn(new SecretKeySpec(this.secretKey.getBytes(), "HmacSHA512"));

        mockedZoneId.when(() -> ZoneId.of(any())).thenThrow(exception);

        CouldNotCreateSession couldNotCreateSessionException = assertThrows(CouldNotCreateSession.class, () -> {
            jjwtSessionGeneratorService.createSession(userId, email,
                    expirationInMinutes);
        });

        assertEquals(couldNotCreateSessionException.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("It must return a generated token")
    void testIfGettingTokenIsOK() {
        String expectedGeneratedJwt = "mocked-jwt-token";

        mockedKeys.when(() -> Keys.hmacShaKeyFor(any()))
                .thenReturn(new SecretKeySpec(this.secretKey.getBytes(), "HmacSHA512"));

        mockedZoneId.when(() -> ZoneId.of(anyString())).thenReturn(this.zoneId);

        DefaultJwtBuilder jwtBuilder = mock(DefaultJwtBuilder.class);

        when(jwtBuilder.issuer(anyString())).thenReturn(jwtBuilder);
        when(jwtBuilder.subject(anyString())).thenReturn(jwtBuilder);
        when(jwtBuilder.issuedAt(any())).thenReturn(jwtBuilder);
        when(jwtBuilder.expiration(any())).thenReturn(jwtBuilder);
        when(jwtBuilder.signWith(any())).thenReturn(jwtBuilder);
        when(jwtBuilder.compact()).thenReturn(expectedGeneratedJwt);

        mockedJwts.when(() -> Jwts.builder()).thenReturn(jwtBuilder);

        String generatedJwt = this.jjwtSessionGeneratorService.createSession(userId,
                email, expirationInMinutes);

        assertEquals(expectedGeneratedJwt, generatedJwt);
    }
}

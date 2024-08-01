package com.springtodo.core.authentication.infrastructure.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        this.userId = UUID.randomUUID().toString();
        this.email = "someemail@email.com";
        this.expirationInMinutes = 30;

        jjwtSessionGeneratorService = new JjwtSessionGeneratorService(secretKey, applicationTimeZone,
                sessionIssuer,
                sessionSubject);
    }

    @Test
    @DisplayName("It must thrown an error when getting hmac sha key fails")
    void testWhenJwtGenerationFails() {
        WeakKeyException exception = new WeakKeyException("An error has occured");

        MockedStatic<Keys> mockedKeys = mockStatic(Keys.class);

        mockedKeys.when(() -> Keys.hmacShaKeyFor(any())).thenThrow(exception);

        JjwtSessionGeneratorService jjwtSessionGeneratorService = new JjwtSessionGeneratorService(secretKey,
                applicationTimeZone, sessionIssuer,
                sessionSubject);

        mockedKeys.close();

        assertThrows(CouldNotCreateSession.class, () -> {
            jjwtSessionGeneratorService.createSession(userId, email, expirationInMinutes);
        });

    }

    @Test
    @DisplayName("It must thrown an error when getting zoned time fails")
    void testeWhenGettingTimeZoneFails() {
        DateTimeException exception = new DateTimeException("A timezone error has occurred");

        MockedStatic<Keys> mockedKeys = mockStatic(Keys.class);

        mockedKeys.when(() -> Keys.hmacShaKeyFor(any()))
                .thenReturn(new SecretKeySpec(this.secretKey.getBytes(), "HmacSHA512"));

        MockedStatic<ZoneId> mockedZoneId = mockStatic(ZoneId.class);

        mockedZoneId.when(() -> ZoneId.of(any())).thenThrow(exception);

        JjwtSessionGeneratorService jjwtSessionGeneratorService = new JjwtSessionGeneratorService(secretKey,
                applicationTimeZone, sessionIssuer,
                sessionSubject);

        mockedKeys.close();
        mockedZoneId.close();

        assertThrows(CouldNotCreateSession.class, () -> {
            jjwtSessionGeneratorService.createSession(userId, email,
                    expirationInMinutes);
        });
    }

    @Test
    @DisplayName("It must return a generated token")
    void testIfGettingTokenIsOK() {
        String expectedGeneratedJwt = "mocked-jwt-token";

        Date currentDate = Date.from(ZonedDateTime.now(ZoneId.of(this.applicationTimeZone)).toInstant());
        Date expirationDate = Date.from(ZonedDateTime.now(ZoneId.of(this.applicationTimeZone))
                .plusMinutes(this.expirationInMinutes).toInstant());

        DefaultJwtBuilder jwtBuilder = Mockito.mock(DefaultJwtBuilder.class);

        when(jwtBuilder.setIssuer(anyString())).thenReturn(jwtBuilder);
        when(jwtBuilder.setSubject(anyString())).thenReturn(jwtBuilder);
        when(jwtBuilder.claim(anyString(), any())).thenReturn(jwtBuilder);
        when(jwtBuilder.setIssuedAt(currentDate)).thenReturn(jwtBuilder);
        when(jwtBuilder.setExpiration(expirationDate)).thenReturn(jwtBuilder);
        when(jwtBuilder.signWith(any())).thenReturn(jwtBuilder);
        when(jwtBuilder.compact()).thenReturn(expectedGeneratedJwt);

        MockedStatic<Jwts> mockedJwts = mockStatic(Jwts.class);

        mockedJwts.when(() -> Jwts.builder()).thenReturn(jwtBuilder);

        String generatedJwt = this.jjwtSessionGeneratorService.createSession(userId,
                email, expirationInMinutes);

        assertEquals(expectedGeneratedJwt, generatedJwt);
    }
}

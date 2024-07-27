package com.springtodo.core.authentication.infrastructure.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

import org.eclipse.jetty.io.QuietException.Exception;
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

import io.jsonwebtoken.impl.DefaultJwtBuilder;
import io.jsonwebtoken.security.Keys;

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

    private MockedStatic<Keys> mockedKeys;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        this.userId = UUID.randomUUID().toString();
        this.email = "someemail@email.com";
        this.expirationInMinutes = 30;

        jjwtSessionGeneratorService = new JjwtSessionGeneratorService(secretKey, applicationTimeZone, sessionIssuer,
                sessionSubject);

        this.mockedKeys = mockStatic(Keys.class);
    }

    @Test
    @DisplayName("It must thrown an error when getting hmac sha key fails")
    void testWhenJwtGenerationFails() {
        Exception exception = new Exception("An error has occured");

        try(){
            
        }

        mockedKeys.when(() -> Keys.hmacShaKeyFor(any())).thenReturn(this.mockedKeys);
        mockedKeys.when(mockedKeys)

        Exception thrownException = assertThrows(Exception.class, () -> {
            jjwtSessionGeneratorService.createSession(userId, email, expirationInMinutes);
        });

        assertEquals(CouldNotCreateSession.class, thrownException.getClass());
    }

    @Test
    @DisplayName("It must thrown an error when getting zoned time fails")
    void testeWhenGettingTimeZoneFails() {
        Exception exception = new Exception("A timezone error has occurred");

        when(ZoneId.of(anyString())).thenThrow(exception);

        Exception thrownException = assertThrows(Exception.class, () -> {
            jjwtSessionGeneratorService.createSession(userId, email, expirationInMinutes);
        });

        assertEquals(CouldNotCreateSession.class, thrownException.getClass());
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

        String generatedJwt = jjwtSessionGeneratorService.createSession(expectedGeneratedJwt, expectedGeneratedJwt,
                expirationInMinutes);

        assertEquals(expectedGeneratedJwt, generatedJwt);
    }
}

package com.springtodo.core.authentication.infrastructure.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.eclipse.jetty.io.QuietException.Exception;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import com.springtodo.core.autentication.infrastructure.service.sessiongenerator.JjwtSessionGeneratorService;

import io.jsonwebtoken.Jwts;

@ExtendWith(MockitoExtension.class)
public class JjwtSessionGeneratorServiceUnitTest {
    @InjectMocks
    private JjwtSessionGeneratorService jjwtSessionGeneratorService;

    @Mock
    private Environment env;

    @Mock
    private Jwts jwts;

    private String userId;
    private String email;
    private String expirationInMinutes;

    @BeforeEach
    void setUp() {
        when(env.getProperty("jwt.SecretKey")).thenReturn("someValue");
        when(env.getProperty("app.Timezone")).thenReturn("America/Sao_Paulo");
        when(env.getProperty("app.SessionIssuer")).thenReturn("todoApp");

        this.userId = UUID.randomUUID().toString();
        this.email = "someemail@email.com";
        this.expirationInMinutes = "30m";
    }

    @Test
    @DisplayName("It must thrown an error when jwt generation fails")
    void testWhenJwtGenerationFails() {
        Exception exception = new Exception("An error has occured");

        when(jwts.builder().compact()).thenThrow(exception);

        Exception thrownException = assertThrows(Exception.class, () -> {
            jjwtSessionGeneratorService.createSession(userId, email, expirationInMinutes);
        });
        assertEquals(exception, thrownException);
    }
}

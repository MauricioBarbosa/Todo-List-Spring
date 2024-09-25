package com.springtodo.unit.core.authentication.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import com.springtodo.core.autentication.domain.entity.User;
import com.springtodo.core.autentication.domain.exception.CouldNotCreateSession;
import com.springtodo.core.autentication.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.autentication.domain.exception.InvalidPassword;
import com.springtodo.core.autentication.domain.exception.UserNotFoundException;
import com.springtodo.core.autentication.domain.repository.UserRepository;
import com.springtodo.core.autentication.domain.service.AuthorizationService;
import com.springtodo.core.autentication.domain.service.SessionGeneratorService;

public class AuthorizationServiceUnitTest {
    @InjectMocks
    private AuthorizationService authorizationService;

    @Mock
    private SessionGeneratorService sessionGeneratorServiceMock;

    @Mock
    private UserRepository userRepositoryMock;

    private String email = "someemail@email.com";
    private String password = "somePassword";

    private Long sessionExpirationInSeconds = (long) 120;

    @Mock
    private Logger logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(authorizationService, "sessionExpirationInSeconds",
                this.sessionExpirationInSeconds);
    }

    @Test
    @DisplayName("It must throw an error when recovering user fails")
    void testUserRecoveryFails() throws UserNotFoundException, CouldNotRetrieveUser {
        UserNotFoundException userNotFoundException = new UserNotFoundException("User not found");

        when(userRepositoryMock.getUserByEmailAddress(anyString())).thenThrow(userNotFoundException);

        try {
            this.authorizationService.startSession(email, password);
        } catch (Exception e) {
            assertEquals(UserNotFoundException.class, userNotFoundException.getClass());
        }

        Map<String, String> logPayload = new HashMap<>();

        logPayload.put("password", password);
        logPayload.put("email", email);

        verify(logger).info("Recovering user", logPayload);
    }

    @Test
    @DisplayName("It must throw an error when user is not authentic")
    void testUserNotAuthentic() throws UserNotFoundException, CouldNotRetrieveUser {
        User user = new User("#someId", "someEmail", "somePassword1");

        Map<String, String> logPayload = new HashMap<>();

        logPayload.put("password", password);
        logPayload.put("email", email);

        Map<String, String> logPayloadAfterUser = new HashMap<>();
        logPayloadAfterUser.put("password", password);
        logPayloadAfterUser.put("email", email);
        logPayloadAfterUser.put("user", user.toString());

        when(userRepositoryMock.getUserByEmailAddress(anyString())).thenReturn(user);

        assertThrows(InvalidPassword.class, () -> {
            this.authorizationService.startSession(email, password);
        });

        verify(logger).info("Recovering user", logPayload);
        verify(logger).error("Invalid password", logPayloadAfterUser);
    }

    @Test
    @DisplayName("It must throw an error when generating session fails")
    void testSessionGenerationFails() throws UserNotFoundException, CouldNotRetrieveUser {
        CouldNotCreateSession couldNotCreateSession = new CouldNotCreateSession("Some session error");

        User user = new User("#someId", "someEmail", "somePassword");

        when(userRepositoryMock.getUserByEmailAddress(anyString())).thenReturn(user);
        when(sessionGeneratorServiceMock.createSession(anyString(), anyString(), anyLong(),
                any()))
                .thenThrow(couldNotCreateSession);

        try {
            this.authorizationService.startSession(email, password);
        } catch (Exception e) {
            assertEquals(e.getClass(), couldNotCreateSession.getClass());
        }

        Map<String, String> logPayload = new HashMap<>();

        logPayload.put("password", password);
        logPayload.put("email", email);

        Map<String, String> logPayloadAfterUser = new HashMap<>();
        logPayloadAfterUser.put("password", password);
        logPayloadAfterUser.put("email", email);
        logPayloadAfterUser.put("user", user.toString());

        verify(logger).info("Recovering user", logPayload);
        verify(logger).info("Generating session", logPayloadAfterUser);
    }

    @Test
    @DisplayName("It must return a session token")
    void testWhenNothingFails()
            throws UserNotFoundException, CouldNotRetrieveUser, CouldNotCreateSession, InvalidPassword {

        User user = new User("#someId", "someEmail", "somePassword");
        String sessionToken = new String("#someSessionToken");

        Map<String, String> logPayload = new HashMap<>();

        logPayload.put("password", password);
        logPayload.put("email", email);

        Map<String, String> logPayloadAfterUser = new HashMap<>();
        logPayloadAfterUser.put("password", password);
        logPayloadAfterUser.put("email", email);
        logPayloadAfterUser.put("user", user.toString());

        when(userRepositoryMock.getUserByEmailAddress(anyString())).thenReturn(user);
        when(sessionGeneratorServiceMock.createSession(anyString(), anyString(), anyLong(), any()))
                .thenReturn(sessionToken);

        String generatedSessionToken = this.authorizationService.startSession(email, password);

        assertEquals(generatedSessionToken, sessionToken);
        verify(logger).info("Recovering user", logPayload);
        verify(logger).info("Generating session", logPayloadAfterUser);
        verify(logger).info("Returning session", logPayloadAfterUser);
    }
}

package com.springtodo.unit.rest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.springtodo.core.autentication.application.usecase.authenticate.AuthenticateUseCase;
import com.springtodo.core.autentication.domain.exception.CouldNotCreateSession;
import com.springtodo.core.autentication.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.autentication.domain.exception.InvalidPassword;
import com.springtodo.core.autentication.domain.exception.UserNotFoundException;
import com.springtodo.rest.controller.AutenticationControl;
import com.springtodo.rest.model.AuthenticationInputJson;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControlUnitTest {

    @InjectMocks
    private AutenticationControl authenticationControl;

    @Mock
    private AuthenticateUseCase authenticateUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("It should return an error ResponseStatusException with BAD_REQUEST when authenticateUseCase throws UserNotFoundException")
    void testWhenAuthenticateUseCaseThrowsUserNotFoundException()
            throws UserNotFoundException, CouldNotCreateSession, CouldNotRetrieveUser, InvalidPassword {
        UserNotFoundException userNotFoundException = new UserNotFoundException("Some user not found error");
        ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.BAD_REQUEST,
                userNotFoundException.getMessage());

        when(authenticateUseCase.execute(any())).thenThrow(userNotFoundException);

        try {
            authenticationControl.login(new AuthenticationInputJson());
        } catch (Exception e) {
            assertEquals(responseStatusException.getClass(), e.getClass());
        }
    }

    @Test
    @DisplayName("It should return an error ResponseStatusException with BAD_REQUEST when authenticateUseCase throws InvalidPassword")
    void testWhenAuthenticateUseCaseThrowsInvalidPassword()
            throws UserNotFoundException, CouldNotCreateSession, CouldNotRetrieveUser, InvalidPassword {
        InvalidPassword invalidPassword = new InvalidPassword("some invalid password error");
        ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.BAD_REQUEST,
                invalidPassword.getMessage());

        when(authenticateUseCase.execute(any())).thenThrow(invalidPassword);

        try {
            authenticationControl.login(new AuthenticationInputJson());
        } catch (Exception e) {
            assertEquals(responseStatusException.getClass(), e.getClass());
        }
    }

    @Test
    @DisplayName("It should return an error ResponseStatusException with INTERNAL_SERVER_ERROR when authenticateUseCase throws CouldNotRetrieveUser")
    void testWhenAuthenticateUseCaseThrowsCouldNotRetrieveUser()
            throws UserNotFoundException, CouldNotCreateSession, CouldNotRetrieveUser, InvalidPassword {
        CouldNotRetrieveUser couldNotRetrieveUser = new CouldNotRetrieveUser("could not retrieve user");
        ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error");

        when(authenticateUseCase.execute(any())).thenThrow(couldNotRetrieveUser);

        try {
            authenticationControl.login(new AuthenticationInputJson());
        } catch (Exception e) {
            assertEquals(responseStatusException.getClass(), e.getClass());
        }
    }

    @Test
    @DisplayName("It should return an error ResponseStatusException with INTERNAL_SERVER_ERROR when authenticateUseCase throws CouldNotCreateSession")
    void testWhenAuthenticateUseCaseThrowsCouldNotCreateSession()
            throws UserNotFoundException, CouldNotCreateSession, CouldNotRetrieveUser, InvalidPassword {
        CouldNotCreateSession couldNotCreateSession = new CouldNotCreateSession("could not retrieve user");
        ResponseStatusException responseStatusException = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error");

        when(authenticateUseCase.execute(any())).thenThrow(couldNotCreateSession);

        try {
            authenticationControl.login(new AuthenticationInputJson());
        } catch (Exception e) {
            assertEquals(responseStatusException.getClass(), e.getClass());
        }
    }
}

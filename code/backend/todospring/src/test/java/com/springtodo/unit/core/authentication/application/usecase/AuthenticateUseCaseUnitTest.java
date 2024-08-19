package com.springtodo.unit.core.authentication.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.springtodo.core.autentication.application.usecase.authenticate.AuthenticateUseCase;
import com.springtodo.core.autentication.application.usecase.authenticate.dto.InputDTO;
import com.springtodo.core.autentication.application.usecase.authenticate.dto.OutputDTO;
import com.springtodo.core.autentication.domain.exception.CouldNotCreateSession;
import com.springtodo.core.autentication.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.autentication.domain.exception.InvalidPassword;
import com.springtodo.core.autentication.domain.exception.UserNotFoundException;
import com.springtodo.core.autentication.domain.service.AuthorizationService;

public class AuthenticateUseCaseUnitTest {

    @InjectMocks
    private AuthenticateUseCase authenticateUseCase;

    @Mock
    private AuthorizationService authorizationServiceMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should thrown an error when AuthorizationService fails")
    void shouldThrowErrorWhenUserRepositoryFails()
            throws UserNotFoundException, CouldNotCreateSession, CouldNotRetrieveUser, InvalidPassword {
        CouldNotCreateSession couldNotCreateSession = new CouldNotCreateSession("An error has occured");

        InputDTO inputDTO = new InputDTO("#someEmail", "#somePassword");

        when(authorizationServiceMock.startSession(inputDTO.getEmail(), inputDTO.getPassword()))
                .thenThrow(couldNotCreateSession);

        try {
            authenticateUseCase.execute(inputDTO);
        } catch (Exception e) {
            assertEquals(couldNotCreateSession.getClass(), e.getClass());
        }
    }

    @Test
    @DisplayName("Should return a token")
    void shouldReturnAGeneratedToken()
            throws UserNotFoundException, CouldNotCreateSession, CouldNotRetrieveUser, InvalidPassword {
        InputDTO inputDTO = new InputDTO("#someEmail", "#somePassword");
        String token = new String("#someSessionToken");
        OutputDTO outputDTO = new OutputDTO(token);

        when(authorizationServiceMock.startSession(inputDTO.getEmail(), inputDTO.getPassword()))
                .thenReturn(token);

        OutputDTO result = authenticateUseCase.execute(inputDTO);

        assertEquals(outputDTO.token, result.token);
    }
}

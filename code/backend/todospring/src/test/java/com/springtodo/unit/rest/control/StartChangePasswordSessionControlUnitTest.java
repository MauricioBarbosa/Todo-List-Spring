package com.springtodo.unit.rest.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.springtodo.core.identity_and_access.application.dto.StartChangePasswordSessionInput;
import com.springtodo.core.identity_and_access.application.dto.StartChangePasswordSessionOutput;
import com.springtodo.core.identity_and_access.application.exception.CouldNotGenerateToken;
import com.springtodo.core.identity_and_access.application.usecase.StartChangePasswordSession;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSaveSession;
import com.springtodo.core.identity_and_access.domain.exception.UserNotFoundException;
import com.springtodo.rest.control.StartChangePasswordSessionControl;
import com.springtodo.rest.pojo.start_change_password_session_control.StartChangePasswordSessionRequestBody;
import com.springtodo.rest.pojo.start_change_password_session_control.StartChangePasswordSessionResponse;

public class StartChangePasswordSessionControlUnitTest {

    @InjectMocks
    private StartChangePasswordSessionControl startChangePasswordSessionControl;

    @Mock
    private StartChangePasswordSession startChangePasswordSessionMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void it_shouldThrowUserNotFoundException()
            throws UserNotFoundException, CouldNotRetrieveUser, CouldNotSaveSession, CouldNotGenerateToken {
        UserNotFoundException userNotFoundException = new UserNotFoundException("user not found");

        String userEmail = "someUserEmail@email.com";

        StartChangePasswordSessionRequestBody startChangePasswordSessionRequestBody = new StartChangePasswordSessionRequestBody();

        startChangePasswordSessionRequestBody.setUserEmail(userEmail);

        when(startChangePasswordSessionMock.execute(any(StartChangePasswordSessionInput.class)))
                .thenThrow(userNotFoundException);

        assertThrows(UserNotFoundException.class, () -> {
            startChangePasswordSessionControl.startChangePasswordSession(startChangePasswordSessionRequestBody);
        });
    }

    @Test
    void it_shouldThrowCouldNotRetrieveUser()
            throws UserNotFoundException, CouldNotRetrieveUser, CouldNotSaveSession, CouldNotGenerateToken {
        CouldNotRetrieveUser couldNotRetrieveUser = new CouldNotRetrieveUser("user not found");

        String userEmail = "someUserEmail@email.com";

        StartChangePasswordSessionRequestBody startChangePasswordSessionRequestBody = new StartChangePasswordSessionRequestBody();

        startChangePasswordSessionRequestBody.setUserEmail(userEmail);

        when(startChangePasswordSessionMock.execute(any(StartChangePasswordSessionInput.class)))
                .thenThrow(couldNotRetrieveUser);

        assertThrows(CouldNotRetrieveUser.class, () -> {
            startChangePasswordSessionControl.startChangePasswordSession(startChangePasswordSessionRequestBody);
        });
    }

    @Test
    void it_shouldThrowCouldNotSaveSession()
            throws UserNotFoundException, CouldNotRetrieveUser, CouldNotSaveSession, CouldNotGenerateToken {
        CouldNotSaveSession couldNotSaveSession = new CouldNotSaveSession("could not save session for whatever reason");

        String userEmail = "someUserEmail@email.com";

        StartChangePasswordSessionRequestBody startChangePasswordSessionRequestBody = new StartChangePasswordSessionRequestBody();

        startChangePasswordSessionRequestBody.setUserEmail(userEmail);

        when(startChangePasswordSessionMock.execute(any(StartChangePasswordSessionInput.class)))
                .thenThrow(couldNotSaveSession);

        assertThrows(CouldNotSaveSession.class, () -> {
            startChangePasswordSessionControl.startChangePasswordSession(startChangePasswordSessionRequestBody);
        });
    }

    @Test
    void it_shouldThrowCouldNotGenerateToken()
            throws UserNotFoundException, CouldNotRetrieveUser, CouldNotSaveSession, CouldNotGenerateToken {
        CouldNotGenerateToken couldNotGenerateToken = new CouldNotGenerateToken(
                "could not generate token for whatever reason");

        String userEmail = "someUserEmail@email.com";

        StartChangePasswordSessionRequestBody startChangePasswordSessionRequestBody = new StartChangePasswordSessionRequestBody();

        startChangePasswordSessionRequestBody.setUserEmail(userEmail);

        when(startChangePasswordSessionMock.execute(any(StartChangePasswordSessionInput.class)))
                .thenThrow(couldNotGenerateToken);

        assertThrows(CouldNotGenerateToken.class, () -> {
            startChangePasswordSessionControl.startChangePasswordSession(startChangePasswordSessionRequestBody);
        });
    }

    @Test
    void it_shouldRunWithoutExceptions()
            throws UserNotFoundException, CouldNotRetrieveUser, CouldNotSaveSession, CouldNotGenerateToken {
        String userEmail = "someUserEmail@email.com";
        String sessionToken = "someSessionToken";

        StartChangePasswordSessionRequestBody startChangePasswordSessionRequestBody = new StartChangePasswordSessionRequestBody();

        StartChangePasswordSessionOutput startChangePasswordSessionOutput = new StartChangePasswordSessionOutput();

        startChangePasswordSessionOutput.setSessionToken(sessionToken);

        startChangePasswordSessionRequestBody.setUserEmail(userEmail);

        when(startChangePasswordSessionMock.execute(any(StartChangePasswordSessionInput.class)))
                .thenReturn(startChangePasswordSessionOutput);

        ResponseEntity<StartChangePasswordSessionResponse> response = startChangePasswordSessionControl
                .startChangePasswordSession(startChangePasswordSessionRequestBody);

        assertEquals(startChangePasswordSessionOutput.getSessionToken(), response.getBody().getSessionToken());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}

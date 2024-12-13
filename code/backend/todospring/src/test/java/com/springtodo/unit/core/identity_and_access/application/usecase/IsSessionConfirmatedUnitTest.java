package com.springtodo.unit.core.identity_and_access.application.usecase;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.springtodo.core.identity_and_access.application.dto.IsSessionConfirmatedInput;
import com.springtodo.core.identity_and_access.application.exception.CouldNotDecodeToken;
import com.springtodo.core.identity_and_access.application.exception.InvalidToken;
import com.springtodo.core.identity_and_access.application.usecase.IsSessionConfirmed;
import com.springtodo.core.identity_and_access.application.utils.SessionTokenGeneratorUtil;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotFindSession;
import com.springtodo.core.identity_and_access.domain.exception.SessionNotFound;
import com.springtodo.core.identity_and_access.domain.service.SessionService;
import com.springtodo.core.identity_and_access.domain.value_object.SessionId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class IsSessionConfirmatedUnitTest {

    @InjectMocks
    private IsSessionConfirmed isSessionConfirmed;

    @Mock
    private SessionService sessionServiceMock;

    @Mock
    private SessionTokenGeneratorUtil sessionTokenGeneratorUtilMock;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldThrowCouldNotDecodeToken()
        throws InvalidToken, CouldNotDecodeToken {
        CouldNotDecodeToken invalidToken = new CouldNotDecodeToken(
            "could not decode token for some reason"
        );

        IsSessionConfirmatedInput isSessionConfirmatedInput =
            new IsSessionConfirmatedInput("someSessionToken");

        when(sessionTokenGeneratorUtilMock.decode(any(String.class))).thenThrow(
            invalidToken
        );

        assertThrows(CouldNotDecodeToken.class, () -> {
            isSessionConfirmed.execute(isSessionConfirmatedInput);
        });
    }

    @Test
    void shouldThrowInvalidToken() throws InvalidToken, CouldNotDecodeToken {
        InvalidToken invalidToken = new InvalidToken();

        IsSessionConfirmatedInput isSessionConfirmatedInput =
            new IsSessionConfirmatedInput("someSessionToken");

        when(sessionTokenGeneratorUtilMock.decode(any(String.class))).thenThrow(
            invalidToken
        );

        assertThrows(InvalidToken.class, () -> {
            isSessionConfirmed.execute(isSessionConfirmatedInput);
        });
    }

    @Test
    void shouldThrowSessionNotFound()
        throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession {
        SessionId sessionId = new SessionId("someSessionId");

        SessionNotFound sessionNotFound = new SessionNotFound(sessionId);

        IsSessionConfirmatedInput isSessionConfirmatedInput =
            new IsSessionConfirmatedInput("someSessionToken");

        when(
            sessionTokenGeneratorUtilMock.decode(any(String.class))
        ).thenReturn(sessionId);

        when(
            sessionServiceMock.isSessionConfirmed(any(SessionId.class))
        ).thenThrow(sessionNotFound);

        assertThrows(SessionNotFound.class, () -> {
            isSessionConfirmed.execute(isSessionConfirmatedInput);
        });
    }

    @Test
    void shouldThrowCouldNotFindSession()
        throws SessionNotFound, CouldNotFindSession, InvalidToken, CouldNotDecodeToken {
        SessionId sessionId = new SessionId("someSessionId");

        CouldNotFindSession couldNotFindSession = new CouldNotFindSession(
            "Could not find session for some reason"
        );

        IsSessionConfirmatedInput isSessionConfirmatedInput =
            new IsSessionConfirmatedInput("someSessionToken");

        when(
            sessionTokenGeneratorUtilMock.decode(any(String.class))
        ).thenReturn(sessionId);

        when(
            sessionServiceMock.isSessionConfirmed(any(SessionId.class))
        ).thenThrow(couldNotFindSession);

        assertThrows(CouldNotFindSession.class, () -> {
            isSessionConfirmed.execute(isSessionConfirmatedInput);
        });
    }

    @Test
    void shouldReturnFalse()
        throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession {
        SessionId sessionId = new SessionId("someSessionId");

        IsSessionConfirmatedInput isSessionConfirmatedInput =
            new IsSessionConfirmatedInput("someSessionToken");

        when(
            sessionTokenGeneratorUtilMock.decode(any(String.class))
        ).thenReturn(sessionId);

        when(
            sessionServiceMock.isSessionConfirmed(any(SessionId.class))
        ).thenReturn(false);

        assertFalse(isSessionConfirmed.execute(isSessionConfirmatedInput));
    }

    @Test
    void shouldReturnTrue() throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession {
        SessionId sessionId = new SessionId("someSessionId");

        IsSessionConfirmatedInput isSessionConfirmatedInput =
            new IsSessionConfirmatedInput("someSessionToken");

        when(
            sessionTokenGeneratorUtilMock.decode(any(String.class))
        ).thenReturn(sessionId);

        when(
            sessionServiceMock.isSessionConfirmed(any(SessionId.class))
        ).thenReturn(true);

        assertTrue(isSessionConfirmed.execute(isSessionConfirmatedInput));
    }
}

package com.springtodo.unit.core.identity_and_access.application.usecase;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.springtodo.core.identity_and_access.application.dto.SendConfirmationCodeInput;
import com.springtodo.core.identity_and_access.application.exception.CouldNotDecodeToken;
import com.springtodo.core.identity_and_access.application.exception.InvalidToken;
import com.springtodo.core.identity_and_access.application.usecase.SendConfirmationCode;
import com.springtodo.core.identity_and_access.application.utils.SessionTokenGeneratorUtil;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotFindSession;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSendEmail;
import com.springtodo.core.identity_and_access.domain.exception.SessionNotFound;
import com.springtodo.core.identity_and_access.domain.exception.UserNotFoundException;
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
public class SendConfirmationCodeUnitTest {

    @InjectMocks
    private SendConfirmationCode sendConfirmationCode;

    @Mock
    private SessionService sessionServiceMock;

    @Mock
    private SessionTokenGeneratorUtil sessionTokenGeneratorUtilMock;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldThrowInvalidToken() throws InvalidToken, CouldNotDecodeToken {
        InvalidToken invalidToken = new InvalidToken();
        SendConfirmationCodeInput sendConfirmationCodeInput =
            new SendConfirmationCodeInput("someSessionToken");

        when(sessionTokenGeneratorUtilMock.decode(any(String.class))).thenThrow(
            invalidToken
        );

        assertThrows(InvalidToken.class, () -> {
            sendConfirmationCode.execute(sendConfirmationCodeInput);
        });
    }

    @Test
    void shouldThrowCouldNotDecodeToken()
        throws InvalidToken, CouldNotDecodeToken {
        CouldNotDecodeToken couldNotDecodeToken = new CouldNotDecodeToken(
            "could not decode token for some reason"
        );
        SendConfirmationCodeInput sendConfirmationCodeInput =
            new SendConfirmationCodeInput("someSessionToken");

        when(sessionTokenGeneratorUtilMock.decode(any(String.class))).thenThrow(
            couldNotDecodeToken
        );

        assertThrows(CouldNotDecodeToken.class, () -> {
            sendConfirmationCode.execute(sendConfirmationCodeInput);
        });
    }

    @Test
    void shouldThrowSessionNotFound()
        throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession, UserNotFoundException, CouldNotRetrieveUser {
        SessionId sessionId = new SessionId("someSessionId");
        SessionNotFound sessionNotFound = new SessionNotFound(sessionId);
        SendConfirmationCodeInput sendConfirmationCodeInput =
            new SendConfirmationCodeInput("someSessionToken");

        when(
            sessionTokenGeneratorUtilMock.decode(any(String.class))
        ).thenReturn(sessionId);

        doThrow(sessionNotFound)
            .when(sessionServiceMock)
            .sendConfirmationCode(any(SessionId.class));

        assertThrows(SessionNotFound.class, () -> {
            sendConfirmationCode.execute(sendConfirmationCodeInput);
        });
    }

    @Test
    void shouldThrowUserNotFound()
        throws SessionNotFound, CouldNotFindSession, UserNotFoundException, CouldNotRetrieveUser, InvalidToken, CouldNotDecodeToken {
        SessionId sessionId = new SessionId("someSessionId");
        UserNotFoundException userNotFound = new UserNotFoundException(
            "something has occurred"
        );
        SendConfirmationCodeInput sendConfirmationCodeInput =
            new SendConfirmationCodeInput("someSessionToken");

        when(
            sessionTokenGeneratorUtilMock.decode(any(String.class))
        ).thenReturn(sessionId);

        doThrow(userNotFound)
            .when(sessionServiceMock)
            .sendConfirmationCode(any(SessionId.class));

        assertThrows(UserNotFoundException.class, () -> {
            sendConfirmationCode.execute(sendConfirmationCodeInput);
        });
    }

    @Test
    void shouldThrowCouldNotRetrieveUser()
        throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession, UserNotFoundException, CouldNotRetrieveUser {
        SessionId sessionId = new SessionId("someSessionId");
        CouldNotRetrieveUser couldNotRetrieveUser = new CouldNotRetrieveUser(
            "something has occurred"
        );
        SendConfirmationCodeInput sendConfirmationCodeInput =
            new SendConfirmationCodeInput("someSessionToken");

        when(
            sessionTokenGeneratorUtilMock.decode(any(String.class))
        ).thenReturn(sessionId);

        doThrow(couldNotRetrieveUser)
            .when(sessionServiceMock)
            .sendConfirmationCode(any(SessionId.class));

        assertThrows(CouldNotRetrieveUser.class, () -> {
            sendConfirmationCode.execute(sendConfirmationCodeInput);
        });
    }

    @Test
    void shouldThrowCouldNotSendEmail()
        throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession, UserNotFoundException, CouldNotRetrieveUser {
        SessionId sessionId = new SessionId("someSessionId");
        CouldNotSendEmail couldNotSendEmail = new CouldNotSendEmail(
            "something has occurred"
        );
        SendConfirmationCodeInput sendConfirmationCodeInput =
            new SendConfirmationCodeInput("someSessionToken");

        when(
            sessionTokenGeneratorUtilMock.decode(any(String.class))
        ).thenReturn(sessionId);

        doThrow(couldNotSendEmail)
            .when(sessionServiceMock)
            .sendConfirmationCode(any(SessionId.class));

        assertThrows(CouldNotSendEmail.class, () -> {
            sendConfirmationCode.execute(sendConfirmationCodeInput);
        });
    }

    @Test
    void shouldRunWithNoExceptions()
        throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession, UserNotFoundException, CouldNotRetrieveUser, CouldNotSendEmail {
        SessionId sessionId = new SessionId("someSessionId");

        SendConfirmationCodeInput sendConfirmationCodeInput =
            new SendConfirmationCodeInput("someSessionToken");

        when(
            sessionTokenGeneratorUtilMock.decode(any(String.class))
        ).thenReturn(sessionId);

        doNothing()
            .when(sessionServiceMock)
            .sendConfirmationCode(any(SessionId.class));

        assertAll(() -> {
            sendConfirmationCode.execute(sendConfirmationCodeInput);
        });
    }
}

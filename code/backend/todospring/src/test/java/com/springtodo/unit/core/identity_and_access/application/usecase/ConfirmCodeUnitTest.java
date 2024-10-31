package com.springtodo.unit.core.identity_and_access.application.usecase;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.springtodo.core.identity_and_access.application.dto.ConfirmCodeInput;
import com.springtodo.core.identity_and_access.application.exception.CouldNotDecodeToken;
import com.springtodo.core.identity_and_access.application.exception.InvalidToken;
import com.springtodo.core.identity_and_access.application.usecase.ConfirmCode;
import com.springtodo.core.identity_and_access.application.utils.SessionTokenGeneratorUtil;
import com.springtodo.core.identity_and_access.domain.exception.ConfirmationCodeIsNotEqualToSessionConfirmationCode;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotFindSession;
import com.springtodo.core.identity_and_access.domain.exception.SessionNotFound;
import com.springtodo.core.identity_and_access.domain.service.SessionService;
import com.springtodo.core.identity_and_access.domain.value_object.ConfirmationCode;
import com.springtodo.core.identity_and_access.domain.value_object.SessionId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ConfirmCodeUnitTest {

    @InjectMocks
    private ConfirmCode confirmCode;

    @Mock
    private SessionService sessionServiceMock;

    @Mock
    private SessionTokenGeneratorUtil sessionTokenGeneratorUtilMock;

    private String confirmationCode = "AXTR4";
    private String sessionToken = "#someSessionToken";

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName(
        "should throw could not decode token once SessionTokenUtils.decode throws CouldNotDecodeToken"
    )
    void shouldThrowCouldNotDecodeToken()
        throws InvalidToken, CouldNotDecodeToken {
        ConfirmCodeInput confirmCodeInput = new ConfirmCodeInput(
            confirmationCode,
            sessionToken
        );

        CouldNotDecodeToken couldNotDecodeToken = new CouldNotDecodeToken(
            "could not decode token for some reason"
        );

        when(sessionTokenGeneratorUtilMock.decode(any(String.class))).thenThrow(
            couldNotDecodeToken
        );

        assertThrows(CouldNotDecodeToken.class, () -> {
            confirmCode.execute(confirmCodeInput);
        });
    }

    @Test
    @DisplayName(
        "should throw invalid session token once SessionTokenUtils.decode throws InvalidToken"
    )
    void shouldThrowInvalidToken() throws InvalidToken, CouldNotDecodeToken {
        ConfirmCodeInput confirmCodeInput = new ConfirmCodeInput(
            confirmationCode,
            sessionToken
        );

        InvalidToken invalidToken = new InvalidToken();

        when(sessionTokenGeneratorUtilMock.decode(any(String.class))).thenThrow(
            invalidToken
        );

        assertThrows(InvalidToken.class, () -> {
            confirmCode.execute(confirmCodeInput);
        });
    }

    @Test
    @DisplayName(
        "it should throw session not found once session service throws SessionNotFound"
    )
    void shoudThrowSessionNotFound()
        throws SessionNotFound, CouldNotFindSession, ConfirmationCodeIsNotEqualToSessionConfirmationCode, InvalidToken, CouldNotDecodeToken {
        SessionId sessionId = new SessionId("#someSessionId");

        SessionNotFound sessionNotFound = new SessionNotFound(sessionId);

        ConfirmCodeInput confirmCodeInput = new ConfirmCodeInput(
            confirmationCode,
            sessionToken
        );

        when(
            sessionTokenGeneratorUtilMock.decode(any(String.class))
        ).thenReturn(sessionId);

        doThrow(sessionNotFound)
            .when(sessionServiceMock)
            .confirmSession(any(SessionId.class), any(ConfirmationCode.class));

        assertThrows(SessionNotFound.class, () -> {
            confirmCode.execute(confirmCodeInput);
        });
    }

    @Test
    @DisplayName(
        "it should throw could not find session once session service throws CouldNotFindSession"
    )
    void shoudThrowCouldNotFindSession()
        throws SessionNotFound, CouldNotFindSession, ConfirmationCodeIsNotEqualToSessionConfirmationCode, InvalidToken, CouldNotDecodeToken {
        SessionId sessionId = new SessionId("#someSessionId");

        CouldNotFindSession couldNotFindSession = new CouldNotFindSession(
            "could not find session for some reason"
        );

        ConfirmCodeInput confirmCodeInput = new ConfirmCodeInput(
            confirmationCode,
            sessionToken
        );

        when(
            sessionTokenGeneratorUtilMock.decode(any(String.class))
        ).thenReturn(sessionId);

        doThrow(couldNotFindSession)
            .when(sessionServiceMock)
            .confirmSession(any(SessionId.class), any(ConfirmationCode.class));

        assertThrows(CouldNotFindSession.class, () -> {
            confirmCode.execute(confirmCodeInput);
        });
    }

    @Test
    @DisplayName(
        "it should throw confirmation code is not equal to session confirmation code once session service throws ConfirmationCodeIsNotEqualToSessionConfirmationCode"
    )
    void shoudThrowConfirmationCodeIsNotEqualToSessionConfirmationCode()
        throws SessionNotFound, CouldNotFindSession, ConfirmationCodeIsNotEqualToSessionConfirmationCode, InvalidToken, CouldNotDecodeToken {
        SessionId sessionId = new SessionId("#someSessionId");

        ConfirmationCodeIsNotEqualToSessionConfirmationCode confirmationCodeIsNotEqualToSessionConfirmationCode =
            new ConfirmationCodeIsNotEqualToSessionConfirmationCode("AXT3");

        ConfirmCodeInput confirmCodeInput = new ConfirmCodeInput(
            confirmationCode,
            sessionToken
        );

        when(
            sessionTokenGeneratorUtilMock.decode(any(String.class))
        ).thenReturn(sessionId);

        doThrow(confirmationCodeIsNotEqualToSessionConfirmationCode)
            .when(sessionServiceMock)
            .confirmSession(any(SessionId.class), any(ConfirmationCode.class));

        assertThrows(
            ConfirmationCodeIsNotEqualToSessionConfirmationCode.class,
            () -> {
                confirmCode.execute(confirmCodeInput);
            }
        );
    }

    @Test
    @DisplayName("should confirm session with no exceptions")
    void shouldConfirmSession()
        throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession, ConfirmationCodeIsNotEqualToSessionConfirmationCode {
        SessionId sessionId = new SessionId("#someSessionId");

        ConfirmCodeInput confirmCodeInput = new ConfirmCodeInput(
            confirmationCode,
            sessionToken
        );

        when(
            sessionTokenGeneratorUtilMock.decode(any(String.class))
        ).thenReturn(sessionId);

        doNothing()
            .when(sessionServiceMock)
            .confirmSession(any(SessionId.class), any(ConfirmationCode.class));

        assertAll(() -> {
            confirmCode.execute(confirmCodeInput);
        });
    }
}

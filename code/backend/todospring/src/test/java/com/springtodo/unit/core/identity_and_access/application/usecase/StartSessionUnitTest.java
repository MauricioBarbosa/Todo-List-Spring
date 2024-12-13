package com.springtodo.unit.core.identity_and_access.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.springtodo.core.identity_and_access.application.dto.StartSessionInput;
import com.springtodo.core.identity_and_access.application.dto.StartSessionOutput;
import com.springtodo.core.identity_and_access.application.exception.CouldNotGenerateToken;
import com.springtodo.core.identity_and_access.application.usecase.StartSession;
import com.springtodo.core.identity_and_access.application.utils.SessionTokenGeneratorUtil;
import com.springtodo.core.identity_and_access.domain.entity.Session;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSaveSession;
import com.springtodo.core.identity_and_access.domain.exception.InvalidPassword;
import com.springtodo.core.identity_and_access.domain.exception.UserNotFoundException;
import com.springtodo.core.identity_and_access.domain.service.SessionService;
import com.springtodo.core.identity_and_access.domain.value_object.ConfirmationCode;
import com.springtodo.core.identity_and_access.domain.value_object.SessionDuration;
import com.springtodo.core.identity_and_access.domain.value_object.SessionId;
import com.springtodo.core.identity_and_access.domain.value_object.SessionStatusStarted;
import com.springtodo.core.identity_and_access.domain.value_object.UserEmail;
import com.springtodo.core.identity_and_access.domain.value_object.UserId;
import com.springtodo.core.identity_and_access.domain.value_object.UserPassword;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StartSessionUnitTest {

    @InjectMocks
    private StartSession startSession;

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionTokenGeneratorUtil sessionTokenGeneratorUtil;

    @Test
    @DisplayName(
        "It should throw UserNotFoundException when SessionService.startSession throws UserNotFoundException"
    )
    void shouldThrowUserNotFoundExceptionOnceSessionServiceThrowsUserNotFoundException()
        throws UserNotFoundException, CouldNotRetrieveUser, InvalidPassword, CouldNotSaveSession {
        String email = "email@email.com";
        String password = "aPassword";

        StartSessionInput startSessionInput = new StartSessionInput(
            email,
            password
        );

        UserNotFoundException userNotFoundException = new UserNotFoundException(
            "user not found for some reason"
        );

        when(
            sessionService.startSession(
                any(UserPassword.class),
                any(UserEmail.class)
            )
        ).thenThrow(userNotFoundException);

        assertThrows(UserNotFoundException.class, () -> {
            startSession.execute(startSessionInput);
        });
    }

    @Test
    @DisplayName(
        "It should throw CouldNotRetrieveUserException when SessionService.startSession throws CouldNotRetrieveUserException"
    )
    void shouldThrowCouldNotRetrieveUserExceptionOnceSessionServiceThrowsCouldNotRetrieveUserException()
        throws UserNotFoundException, CouldNotRetrieveUser, InvalidPassword, CouldNotSaveSession {
        String email = "email@email.com";
        String password = "aPassword";

        StartSessionInput startSessionInput = new StartSessionInput(
            email,
            password
        );

        CouldNotRetrieveUser couldNotRetrieveUser = new CouldNotRetrieveUser(
            "could not retrieve user"
        );

        when(
            sessionService.startSession(
                any(UserPassword.class),
                any(UserEmail.class)
            )
        ).thenThrow(couldNotRetrieveUser);

        assertThrows(CouldNotRetrieveUser.class, () -> {
            startSession.execute(startSessionInput);
        });
    }

    @Test
    @DisplayName(
        "It should throw InvalidPassword when SessionService.startSession throws InvalidPassword"
    )
    void shouldThrowInvalidPasswordOnceSessionServiceThrowsInvalidPassword()
        throws UserNotFoundException, CouldNotRetrieveUser, InvalidPassword, CouldNotSaveSession {
        String email = "email@email.com";
        String password = "aPassword";

        StartSessionInput startSessionInput = new StartSessionInput(
            email,
            password
        );

        InvalidPassword invalidPassword = new InvalidPassword(
            "invalid password"
        );

        when(
            sessionService.startSession(
                any(UserPassword.class),
                any(UserEmail.class)
            )
        ).thenThrow(invalidPassword);

        assertThrows(InvalidPassword.class, () -> {
            startSession.execute(startSessionInput);
        });
    }

    @Test
    @DisplayName(
        "It should throw CouldNotSaveSession when SessionService.startSession throws CouldNotSaveSession"
    )
    void shouldThrowCouldNotSaveSessionOnceSessionServiceThrowsCouldNotSaveSession()
        throws UserNotFoundException, CouldNotRetrieveUser, InvalidPassword, CouldNotSaveSession {
        String email = "email@email.com";
        String password = "aPassword";

        StartSessionInput startSessionInput = new StartSessionInput(
            email,
            password
        );

        CouldNotSaveSession couldNotSaveSession = new CouldNotSaveSession(
            "could not save session for some reason"
        );

        when(
            sessionService.startSession(
                any(UserPassword.class),
                any(UserEmail.class)
            )
        ).thenThrow(couldNotSaveSession);

        assertThrows(CouldNotSaveSession.class, () -> {
            startSession.execute(startSessionInput);
        });
    }

    @Test
    @DisplayName(
        "It should throw an CouldNotGenerateToken when SessionTokenGeneratorUtil.generate throws CouldNotGenerateToken"
    )
    void shouldThrowExceptionOnceSessionTokenGeneratorUtilThrowsException()
        throws UserNotFoundException, CouldNotRetrieveUser, InvalidPassword, CouldNotSaveSession, CouldNotGenerateToken {
        String email = "email@email.com";
        String password = "aPassword";

        StartSessionInput startSessionInput = new StartSessionInput(
            email,
            password
        );

        Long durationInSeconds = Long.valueOf(10);

        Session session = new Session(
            new SessionId(),
            new UserId(),
            new SessionDuration(durationInSeconds),
            new SessionStatusStarted(),
            new ConfirmationCode("AXTR7")
        );

        CouldNotGenerateToken couldNotGenerateToken = new CouldNotGenerateToken(
            "could not generate token"
        );

        when(
            sessionTokenGeneratorUtil.generate(
                any(String.class),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
            )
        ).thenThrow(couldNotGenerateToken);

        when(
            sessionService.startSession(
                any(UserPassword.class),
                any(UserEmail.class)
            )
        ).thenReturn(session);

        assertThrows(CouldNotGenerateToken.class, () -> {
            startSession.execute(startSessionInput);
        });
    }

    @Test
    @DisplayName("It should return encrypted token")
    void shouldReturnEncryptedToken()
        throws UserNotFoundException, CouldNotRetrieveUser, InvalidPassword, CouldNotSaveSession, CouldNotGenerateToken {
        String email = "email@email.com";
        String password = "aPassword";

        String token = "#generatedToken";

        StartSessionInput startSessionInput = new StartSessionInput(
            email,
            password
        );

        StartSessionOutput startSessionOutput = new StartSessionOutput(token);

        Long durationInSeconds = Long.valueOf(10);

        Session session = new Session(
            new SessionId(),
            new UserId(),
            new SessionDuration(durationInSeconds),
            new SessionStatusStarted(),
            new ConfirmationCode("AXTR7")
        );

        when(
            sessionService.startSession(
                any(UserPassword.class),
                any(UserEmail.class)
            )
        ).thenReturn(session);

        when(
            sessionTokenGeneratorUtil.generate(
                any(String.class),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
            )
        ).thenReturn(token);

        StartSessionOutput generatedSessionTokenOutput = startSession.execute(
            startSessionInput
        );

        String sessionToken = generatedSessionTokenOutput.getSessionToken();
        assertEquals(token, sessionToken);
    }
}

package com.springtodo.unit.core.identity_and_access.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.springtodo.core.identity_and_access.application.dto.StartChangePasswordSessionInput;
import com.springtodo.core.identity_and_access.application.dto.StartChangePasswordSessionOutput;
import com.springtodo.core.identity_and_access.application.exception.CouldNotGenerateToken;
import com.springtodo.core.identity_and_access.application.usecase.StartChangePasswordSession;
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

@ExtendWith(MockitoExtension.class)
public class StartChangePasswordSessionUnitTest {

        @Mock
        private SessionService sessionService;

        @Mock
        private SessionTokenGeneratorUtil sessionTokenGeneratorUtil;

        @InjectMocks
        private StartChangePasswordSession startChangePasswordSession;

        @BeforeEach
        void init() {
                MockitoAnnotations.openMocks(this);
        }

        @Test
        void shouldThrowUserNotFoundExceptionOnceSessionServiceThrowsUserNotFoundException()
                        throws UserNotFoundException, CouldNotRetrieveUser, CouldNotSaveSession {
                String email = "email@email.com";

                StartChangePasswordSessionInput startChangePasswordSessionInput = new StartChangePasswordSessionInput();

                startChangePasswordSessionInput.setUserEmail(email);

                UserNotFoundException userNotFoundException = new UserNotFoundException(
                                "user not found for some reason");

                when(
                                sessionService.startChangePasswordSession(
                                                any(UserEmail.class)))
                                .thenThrow(userNotFoundException);

                assertThrows(UserNotFoundException.class, () -> {
                        startChangePasswordSession.execute(startChangePasswordSessionInput);
                });
        }

        @Test
        void shouldThrowCouldNotRetrieveUserExceptionOnceSessionServiceThrowsCouldNotRetrieveUserException()
                        throws UserNotFoundException, CouldNotRetrieveUser, CouldNotSaveSession {
                String email = "email@email.com";

                StartChangePasswordSessionInput startChangePasswordSessionInput = new StartChangePasswordSessionInput();

                startChangePasswordSessionInput.setUserEmail(email);

                CouldNotRetrieveUser couldNotRetrieveUser = new CouldNotRetrieveUser("some error has occurred");

                when(
                                sessionService.startChangePasswordSession(
                                                any(UserEmail.class)))
                                .thenThrow(couldNotRetrieveUser);

                assertThrows(CouldNotRetrieveUser.class, () -> {
                        startChangePasswordSession.execute(startChangePasswordSessionInput);
                });
        }

        @Test
        void shouldThrowCouldNotSaveSessionOnceSessionServiceThrowsCouldNotSaveSession()
                        throws UserNotFoundException, CouldNotRetrieveUser, CouldNotSaveSession {
                String email = "email@email.com";

                StartChangePasswordSessionInput startChangePasswordSessionInput = new StartChangePasswordSessionInput();

                startChangePasswordSessionInput.setUserEmail(email);

                CouldNotSaveSession couldNotSaveSession = new CouldNotSaveSession("some error has occurred");

                when(
                                sessionService.startChangePasswordSession(
                                                any(UserEmail.class)))
                                .thenThrow(couldNotSaveSession);

                assertThrows(CouldNotSaveSession.class, () -> {
                        startChangePasswordSession.execute(startChangePasswordSessionInput);
                });
        }

        @Test
        void shouldThrowExceptionOnceSessionTokenGeneratorUtilThrowsException() throws CouldNotGenerateToken,
                        UserNotFoundException, CouldNotRetrieveUser, InvalidPassword, CouldNotSaveSession {
                String email = "email@email.com";

                StartChangePasswordSessionInput startChangePasswordSessionInput = new StartChangePasswordSessionInput();

                startChangePasswordSessionInput.setUserEmail(email);

                CouldNotGenerateToken couldNotGenerateToken = new CouldNotGenerateToken(
                                "could not generate token");

                Long durationInSeconds = Long.valueOf(10);

                Session session = new Session(
                                new SessionId(),
                                new UserId(),
                                new SessionDuration(durationInSeconds),
                                new SessionStatusStarted(),
                                new ConfirmationCode("AXTR7"));

                when(
                                sessionTokenGeneratorUtil.generate(
                                                any(String.class),
                                                any(LocalDateTime.class),
                                                any(LocalDateTime.class)))
                                .thenThrow(couldNotGenerateToken);

                when(
                                sessionService.startChangePasswordSession(
                                                any(UserEmail.class)))
                                .thenReturn(session);

                assertThrows(CouldNotGenerateToken.class, () -> {
                        startChangePasswordSession.execute(startChangePasswordSessionInput);
                });
        }

        @Test
        void shouldReturnEncryptedToken() throws CouldNotGenerateToken, UserNotFoundException, CouldNotRetrieveUser,
                        InvalidPassword, CouldNotSaveSession {
                String email = "email@email.com";
                String token = "#generatedToken";

                StartChangePasswordSessionInput startChangePasswordSessionInput = new StartChangePasswordSessionInput();

                startChangePasswordSessionInput.setUserEmail(email);

                Long durationInSeconds = Long.valueOf(10);

                Session session = new Session(
                                new SessionId(),
                                new UserId(),
                                new SessionDuration(durationInSeconds),
                                new SessionStatusStarted(),
                                new ConfirmationCode("AXTR7"));

                when(
                                sessionTokenGeneratorUtil.generate(
                                                any(String.class),
                                                any(LocalDateTime.class),
                                                any(LocalDateTime.class)))
                                .thenReturn(token);

                when(
                                sessionService.startChangePasswordSession(
                                                any(UserEmail.class)))
                                .thenReturn(session);

                StartChangePasswordSessionOutput startChangePasswordSessionOutput = startChangePasswordSession.execute(
                                startChangePasswordSessionInput);

                String sessionToken = startChangePasswordSessionOutput.getSessionToken();
                assertEquals(token, sessionToken);
        }
}

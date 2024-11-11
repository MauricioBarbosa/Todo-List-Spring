package com.springtodo.unit.rest.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.springtodo.core.identity_and_access.application.dto.ConfirmCodeInput;
import com.springtodo.core.identity_and_access.application.dto.SendConfirmationCodeInput;
import com.springtodo.core.identity_and_access.application.dto.StartSessionInput;
import com.springtodo.core.identity_and_access.application.dto.StartSessionOutput;
import com.springtodo.core.identity_and_access.application.exception.CouldNotDecodeToken;
import com.springtodo.core.identity_and_access.application.exception.CouldNotGenerateToken;
import com.springtodo.core.identity_and_access.application.exception.InvalidToken;
import com.springtodo.core.identity_and_access.application.usecase.ConfirmCode;
import com.springtodo.core.identity_and_access.application.usecase.SendConfirmationCode;
import com.springtodo.core.identity_and_access.application.usecase.StartSession;
import com.springtodo.core.identity_and_access.domain.exception.ConfirmationCodeIsNotEqualToSessionConfirmationCode;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotFindSession;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSaveSession;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSendEmail;
import com.springtodo.core.identity_and_access.domain.exception.InvalidPassword;
import com.springtodo.core.identity_and_access.domain.exception.SessionNotFound;
import com.springtodo.core.identity_and_access.domain.exception.UserNotFoundException;
import com.springtodo.core.identity_and_access.domain.value_object.SessionId;
import com.springtodo.rest.control.SessionControl;
import com.springtodo.rest.pojo.session_control.ConfirmSessionInput;
import com.springtodo.rest.pojo.session_control.LoginInput;
import com.springtodo.rest.pojo.session_control.LoginOutput;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class SessionControlUnitTest {

    @InjectMocks
    private SessionControl sessionControl;

    @Mock
    private StartSession startSessionUseCase;

    @Mock
    private ConfirmCode confirmCodeUseCase;

    @Mock
    private SendConfirmationCode sendConfirmationCode;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Testing SessionControl.login")
    class TestPostLogin {

        private String email = "someemail@email.com";

        private String password = "somePassword";

        @Test
        void shouldThrowUserNotFoundException()
            throws UserNotFoundException, CouldNotRetrieveUser, InvalidPassword, CouldNotSaveSession, CouldNotGenerateToken {
            LoginInput loginInput = new LoginInput();

            loginInput.setEmail(email);

            loginInput.setPassword(password);

            UserNotFoundException couldNotRetrieveUser =
                new UserNotFoundException("user not found");

            when(
                startSessionUseCase.execute(any(StartSessionInput.class))
            ).thenThrow(couldNotRetrieveUser);

            assertThrows(UserNotFoundException.class, () -> {
                sessionControl.login(loginInput);
            });
        }

        @Test
        void shouldThrowCouldNotRetrieveUser()
            throws UserNotFoundException, CouldNotRetrieveUser, InvalidPassword, CouldNotSaveSession, CouldNotGenerateToken {
            LoginInput loginInput = new LoginInput();

            loginInput.setEmail(email);

            loginInput.setPassword(password);

            CouldNotRetrieveUser couldNotRetrieveUser =
                new CouldNotRetrieveUser(
                    "could not retrieve user for some reason"
                );

            when(
                startSessionUseCase.execute(any(StartSessionInput.class))
            ).thenThrow(couldNotRetrieveUser);

            assertThrows(CouldNotRetrieveUser.class, () -> {
                sessionControl.login(loginInput);
            });
        }

        @Test
        void shouldThrowInvalidPassword()
            throws UserNotFoundException, CouldNotRetrieveUser, InvalidPassword, CouldNotSaveSession, CouldNotGenerateToken {
            LoginInput loginInput = new LoginInput();

            loginInput.setEmail(email);

            loginInput.setPassword(password);

            InvalidPassword couldNotRetrieveUser = new InvalidPassword(
                "the informed password is invalid"
            );

            when(
                startSessionUseCase.execute(any(StartSessionInput.class))
            ).thenThrow(couldNotRetrieveUser);

            assertThrows(InvalidPassword.class, () -> {
                sessionControl.login(loginInput);
            });
        }

        @Test
        void shouldThrowCouldNotSaveSession()
            throws UserNotFoundException, CouldNotRetrieveUser, InvalidPassword, CouldNotSaveSession, CouldNotGenerateToken {
            LoginInput loginInput = new LoginInput();

            loginInput.setEmail(email);

            loginInput.setPassword(password);

            CouldNotSaveSession couldNotSaveSession = new CouldNotSaveSession(
                "could not save session for some reason"
            );

            when(
                startSessionUseCase.execute(any(StartSessionInput.class))
            ).thenThrow(couldNotSaveSession);

            assertThrows(CouldNotSaveSession.class, () -> {
                sessionControl.login(loginInput);
            });
        }

        @Test
        void shouldThrowCouldNotGenerateToken()
            throws UserNotFoundException, CouldNotRetrieveUser, InvalidPassword, CouldNotSaveSession, CouldNotGenerateToken {
            LoginInput loginInput = new LoginInput();

            loginInput.setEmail(email);

            loginInput.setPassword(password);

            CouldNotGenerateToken couldNotSaveSession =
                new CouldNotGenerateToken(
                    "could not generate token for some reason"
                );

            when(
                startSessionUseCase.execute(any(StartSessionInput.class))
            ).thenThrow(couldNotSaveSession);

            assertThrows(CouldNotGenerateToken.class, () -> {
                sessionControl.login(loginInput);
            });
        }

        @Test
        void shouldReturnAResponseEntityWithToken()
            throws UserNotFoundException, CouldNotRetrieveUser, InvalidPassword, CouldNotSaveSession, CouldNotGenerateToken {
            LoginInput loginInput = new LoginInput();

            StartSessionOutput startSessionOutput = new StartSessionOutput(
                "someSessionToken"
            );

            loginInput.setEmail(email);

            loginInput.setPassword(password);

            when(
                startSessionUseCase.execute(any(StartSessionInput.class))
            ).thenReturn(startSessionOutput);

            ResponseEntity<LoginOutput> response = sessionControl.login(
                loginInput
            );

            assertEquals(response.getStatusCode(), HttpStatus.OK);
            assertEquals(
                response.getBody().getSessionToken(),
                startSessionOutput.getSessionToken()
            );
        }
    }

    @Nested
    @DisplayName("Testing SessionControl.confirmSession")
    class TestPostConfirmSession {

        private String confirmationCode = "AXRT7";
        private String sessionToken = "someSessionToken";

        @Test
        void shouldThrowInvalidToken()
            throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession, ConfirmationCodeIsNotEqualToSessionConfirmationCode {
            InvalidToken invalidToken = new InvalidToken();

            ConfirmSessionInput confirmSessionInput = new ConfirmSessionInput();

            confirmSessionInput.setConfirmationCode(confirmationCode);

            doThrow(invalidToken)
                .when(confirmCodeUseCase)
                .execute(any(ConfirmCodeInput.class));

            assertThrows(InvalidToken.class, () -> {
                sessionControl.confirmSession(
                    sessionToken,
                    confirmSessionInput
                );
            });
        }

        @Test
        void shouldThrowCouldNotDecodeToken()
            throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession, ConfirmationCodeIsNotEqualToSessionConfirmationCode {
            CouldNotDecodeToken couldNotDecodeToken = new CouldNotDecodeToken(
                "could not decode token for some reason"
            );

            ConfirmSessionInput confirmSessionInput = new ConfirmSessionInput();

            confirmSessionInput.setConfirmationCode(confirmationCode);

            doThrow(couldNotDecodeToken)
                .when(confirmCodeUseCase)
                .execute(any(ConfirmCodeInput.class));

            assertThrows(CouldNotDecodeToken.class, () -> {
                sessionControl.confirmSession(
                    sessionToken,
                    confirmSessionInput
                );
            });
        }

        @Test
        void shouldThrowSessionNotFound()
            throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession, ConfirmationCodeIsNotEqualToSessionConfirmationCode {
            SessionId sessionId = new SessionId("someSessionId");

            SessionNotFound sessionNotFound = new SessionNotFound(sessionId);

            ConfirmSessionInput confirmSessionInput = new ConfirmSessionInput();

            confirmSessionInput.setConfirmationCode(confirmationCode);

            doThrow(sessionNotFound)
                .when(confirmCodeUseCase)
                .execute(any(ConfirmCodeInput.class));

            assertThrows(SessionNotFound.class, () -> {
                sessionControl.confirmSession(
                    sessionToken,
                    confirmSessionInput
                );
            });
        }

        @Test
        void shouldThrowCouldNotFindSession()
            throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession, ConfirmationCodeIsNotEqualToSessionConfirmationCode {
            CouldNotFindSession couldNotFindSession = new CouldNotFindSession(
                "could not find session for some reason"
            );

            ConfirmSessionInput confirmSessionInput = new ConfirmSessionInput();

            confirmSessionInput.setConfirmationCode(confirmationCode);

            doThrow(couldNotFindSession)
                .when(confirmCodeUseCase)
                .execute(any(ConfirmCodeInput.class));

            assertThrows(CouldNotFindSession.class, () -> {
                sessionControl.confirmSession(
                    sessionToken,
                    confirmSessionInput
                );
            });
        }

        @Test
        void shouldThrowConfirmationCodeIsNotEqualToSessionConfirmationCode()
            throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession, ConfirmationCodeIsNotEqualToSessionConfirmationCode {
            ConfirmationCodeIsNotEqualToSessionConfirmationCode confirmationCodeIsNotEqualToSessionConfirmationCode =
                new ConfirmationCodeIsNotEqualToSessionConfirmationCode(
                    "confirmation codes are not equals"
                );

            ConfirmSessionInput confirmSessionInput = new ConfirmSessionInput();

            confirmSessionInput.setConfirmationCode(confirmationCode);

            doThrow(confirmationCodeIsNotEqualToSessionConfirmationCode)
                .when(confirmCodeUseCase)
                .execute(any(ConfirmCodeInput.class));

            assertThrows(
                ConfirmationCodeIsNotEqualToSessionConfirmationCode.class,
                () -> {
                    sessionControl.confirmSession(
                        sessionToken,
                        confirmSessionInput
                    );
                }
            );
        }

        @Test
        void shouldReturnASuccessResponse()
            throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession, ConfirmationCodeIsNotEqualToSessionConfirmationCode {
            ConfirmSessionInput confirmSessionInput = new ConfirmSessionInput();

            confirmSessionInput.setConfirmationCode(confirmationCode);

            doNothing()
                .when(confirmCodeUseCase)
                .execute(any(ConfirmCodeInput.class));

            ResponseEntity<Void> response = sessionControl.confirmSession(
                sessionToken,
                confirmSessionInput
            );

            assertEquals(response.getStatusCode(), HttpStatus.OK);
        }
    }

    @Nested
    @DisplayName("Testing SessionControl.sendConfirmationCode")
    class TestPostSendConfirmationCode {

        private String sessionToken = "someSessionToken";

        @Test
        void shouldInvalidToken()
            throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession, UserNotFoundException, CouldNotRetrieveUser, CouldNotSendEmail {
            InvalidToken invalidToken = new InvalidToken();

            doThrow(invalidToken)
                .when(sendConfirmationCode)
                .execute(any(SendConfirmationCodeInput.class));

            assertThrows(InvalidToken.class, () -> {
                sessionControl.sendConfirmationCode(sessionToken);
            });
        }

        @Test
        void shouldCouldNotDecodeToken()
            throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession, UserNotFoundException, CouldNotRetrieveUser, CouldNotSendEmail {
            CouldNotDecodeToken couldNotDecodeToken = new CouldNotDecodeToken(
                "could not decode token for some reason"
            );

            doThrow(couldNotDecodeToken)
                .when(sendConfirmationCode)
                .execute(any(SendConfirmationCodeInput.class));

            assertThrows(CouldNotDecodeToken.class, () -> {
                sessionControl.sendConfirmationCode(sessionToken);
            });
        }

        @Test
        void shouldSessionNotFound()
            throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession, UserNotFoundException, CouldNotRetrieveUser, CouldNotSendEmail {
            SessionId sessionId = new SessionId("someSessionId");

            SessionNotFound sessionNotFound = new SessionNotFound(sessionId);

            doThrow(sessionNotFound)
                .when(sendConfirmationCode)
                .execute(any(SendConfirmationCodeInput.class));

            assertThrows(SessionNotFound.class, () -> {
                sessionControl.sendConfirmationCode(sessionToken);
            });
        }

        @Test
        void shouldCouldNotFindSession()
            throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession, UserNotFoundException, CouldNotRetrieveUser, CouldNotSendEmail {
            CouldNotFindSession couldNotFindSession = new CouldNotFindSession(
                "could not find session for some reason"
            );

            doThrow(couldNotFindSession)
                .when(sendConfirmationCode)
                .execute(any(SendConfirmationCodeInput.class));

            assertThrows(CouldNotFindSession.class, () -> {
                sessionControl.sendConfirmationCode(sessionToken);
            });
        }

        @Test
        void shouldUserNotFoundException()
            throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession, UserNotFoundException, CouldNotRetrieveUser, CouldNotSendEmail {
            UserNotFoundException userNotFoundException =
                new UserNotFoundException(
                    "could not find user for some reason"
                );

            doThrow(userNotFoundException)
                .when(sendConfirmationCode)
                .execute(any(SendConfirmationCodeInput.class));

            assertThrows(UserNotFoundException.class, () -> {
                sessionControl.sendConfirmationCode(sessionToken);
            });
        }

        @Test
        void shouldCouldNotRetrieveUser()
            throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession, UserNotFoundException, CouldNotRetrieveUser, CouldNotSendEmail {
            CouldNotRetrieveUser couldNotRetrieveUser =
                new CouldNotRetrieveUser("could not find user for some reason");

            doThrow(couldNotRetrieveUser)
                .when(sendConfirmationCode)
                .execute(any(SendConfirmationCodeInput.class));

            assertThrows(CouldNotRetrieveUser.class, () -> {
                sessionControl.sendConfirmationCode(sessionToken);
            });
        }

        @Test
        void shouldCouldNotSendEmail()
            throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession, UserNotFoundException, CouldNotRetrieveUser, CouldNotSendEmail {
            CouldNotSendEmail couldNotRetrieveUser = new CouldNotSendEmail(
                "could not send email for some reason"
            );

            doThrow(couldNotRetrieveUser)
                .when(sendConfirmationCode)
                .execute(any(SendConfirmationCodeInput.class));

            assertThrows(CouldNotSendEmail.class, () -> {
                sessionControl.sendConfirmationCode(sessionToken);
            });
        }

        @Test
        void shouldReturnASuccessResponse()
            throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession, UserNotFoundException, CouldNotRetrieveUser, CouldNotSendEmail {
            doNothing()
                .when(sendConfirmationCode)
                .execute(any(SendConfirmationCodeInput.class));

            ResponseEntity<Void> response = sessionControl.sendConfirmationCode(
                sessionToken
            );

            assertEquals(response.getStatusCode(), HttpStatus.OK);
        }
    }
}

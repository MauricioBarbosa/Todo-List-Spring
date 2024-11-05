package com.springtodo.unit.core.identity_and_access.domain.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.springtodo.core.identity_and_access.domain.entity.Session;
import com.springtodo.core.identity_and_access.domain.entity.User;
import com.springtodo.core.identity_and_access.domain.exception.ConfirmationCodeIsNotEqualToSessionConfirmationCode;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotFindSession;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSaveSession;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSendEmail;
import com.springtodo.core.identity_and_access.domain.exception.EmailNotInformed;
import com.springtodo.core.identity_and_access.domain.exception.IdNotInformed;
import com.springtodo.core.identity_and_access.domain.exception.InvalidPassword;
import com.springtodo.core.identity_and_access.domain.exception.PasswordNotInformed;
import com.springtodo.core.identity_and_access.domain.exception.SessionNotFound;
import com.springtodo.core.identity_and_access.domain.exception.UserNotFoundException;
import com.springtodo.core.identity_and_access.domain.provider.EmailSenderProvider;
import com.springtodo.core.identity_and_access.domain.repository.SessionRepository;
import com.springtodo.core.identity_and_access.domain.repository.UserRepository;
import com.springtodo.core.identity_and_access.domain.service.SessionService;
import com.springtodo.core.identity_and_access.domain.value_object.ConfirmationCode;
import com.springtodo.core.identity_and_access.domain.value_object.SessionDuration;
import com.springtodo.core.identity_and_access.domain.value_object.SessionId;
import com.springtodo.core.identity_and_access.domain.value_object.SessionStatus;
import com.springtodo.core.identity_and_access.domain.value_object.UserEmail;
import com.springtodo.core.identity_and_access.domain.value_object.UserId;
import com.springtodo.core.identity_and_access.domain.value_object.UserPassword;
import com.springtodo.unit.core.identity_and_access.domain.builder.SessionBuilder;
import com.springtodo.unit.core.identity_and_access.domain.builder.UserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class SessionServiceUnitTest {

    @Mock
    private SessionRepository sessionRepositoryMock;

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private EmailSenderProvider emailSenderProvider;

    @Mock
    private Logger logger;

    private User user;
    private Long sessionDurationInSeconds;
    private int confirmationCodeSize;
    private String userEmail;
    private String userPassword;

    @InjectMocks
    private SessionService sessionService;

    @BeforeEach
    void init() {
        this.userEmail = "someUserMail@email.com";
        this.userPassword = "#someUserPassword";
        this.sessionDurationInSeconds = Long.valueOf(3600);
        this.confirmationCodeSize = 5;

        this.user = new User(
            new UserId(),
            new UserEmail(userEmail),
            new UserPassword(userPassword)
        );

        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(
            sessionService,
            "sessionDurationInSeconds",
            this.sessionDurationInSeconds
        );

        ReflectionTestUtils.setField(
            sessionService,
            "confirmationCodeSize",
            this.confirmationCodeSize
        );
    }

    @Test
    @DisplayName(
        "It should thrown CouldNotRetrieveUserException once repository throws CouldNotRetrieveUserException"
    )
    void shouldThrowCouldNotRetrieveUserException()
        throws UserNotFoundException, CouldNotRetrieveUser, InvalidPassword {
        UserEmail aUserEmail = new UserEmail("someuseremail@email.com");
        UserPassword aUserPassword = new UserPassword("#someUserPassword");
        CouldNotRetrieveUser couldNotRetrieveUserException =
            new CouldNotRetrieveUser("Could not retrieve user for some reason");

        when(userRepositoryMock.getUserByEmail(aUserEmail)).thenThrow(
            couldNotRetrieveUserException
        );

        assertThrows(CouldNotRetrieveUser.class, () -> {
            sessionService.startSession(aUserPassword, aUserEmail);
        });
    }

    @Test
    @DisplayName(
        "It should thrown UserNotFoundException once repository throws UserNotFoundException"
    )
    void shouldThrowUserNotFoundException()
        throws UserNotFoundException, CouldNotRetrieveUser, InvalidPassword {
        UserEmail aUserEmail = new UserEmail("someuseremail@email.com");
        UserPassword aUserPassword = new UserPassword("#someUserPassword");
        UserNotFoundException userNotFoundException = new UserNotFoundException(
            "Could not find user"
        );

        when(userRepositoryMock.getUserByEmail(aUserEmail)).thenThrow(
            userNotFoundException
        );

        assertThrows(UserNotFoundException.class, () -> {
            sessionService.startSession(aUserPassword, aUserEmail);
        });
    }

    @Test
    @DisplayName("It should thrown InvalidPasswordException")
    public void shouldThrowInvalidPasswordException()
        throws UserNotFoundException, CouldNotRetrieveUser, InvalidPassword {
        UserEmail anUserEmail = new UserEmail("someuseremail@email.com");
        UserPassword anUserPassword = new UserPassword("#someUserPassword2");

        when(userRepositoryMock.getUserByEmail(anUserEmail)).thenReturn(user);

        assertThrows(InvalidPassword.class, () -> {
            sessionService.startSession(anUserPassword, anUserEmail);
        });
    }

    @Test
    @DisplayName("It should thrown CouldNotSaveSessionException")
    void shouldThrowCouldNotSaveSessionException()
        throws UserNotFoundException, CouldNotRetrieveUser, InvalidPassword, CouldNotSaveSession {
        UserEmail aUserEmail = new UserEmail("someuseremail@email.com");
        UserPassword aUserPassword = new UserPassword("#someUserPassword");
        CouldNotSaveSession couldNotSaveSession = new CouldNotSaveSession(
            "Could not save session for some reason"
        );

        when(userRepositoryMock.getUserByEmail(aUserEmail)).thenReturn(
            this.user
        );

        doThrow(couldNotSaveSession)
            .when(sessionRepositoryMock)
            .save(any(Session.class));

        assertThrows(CouldNotSaveSession.class, () -> {
            sessionService.startSession(aUserPassword, aUserEmail);
        });
    }

    @Test
    @DisplayName("It should return a session")
    void shouldReturnASession()
        throws UserNotFoundException, CouldNotRetrieveUser, InvalidPassword, CouldNotSaveSession {
        UserEmail aUserEmail = new UserEmail("someuseremail@email.com");
        UserPassword aUserPassword = new UserPassword("#someUserPassword");

        when(userRepositoryMock.getUserByEmail(aUserEmail)).thenReturn(
            this.user
        );

        doNothing().when(sessionRepositoryMock).save(any(Session.class));

        Session session = sessionService.startSession(
            aUserPassword,
            aUserEmail
        );

        assertEquals(session.getUserId(), this.user.getId());
    }

    @Test
    @DisplayName("It should throw could not find session")
    void shouldThrowCouldNotFindSession()
        throws SessionNotFound, CouldNotFindSession {
        CouldNotFindSession couldNotFindSession = new CouldNotFindSession(
            "Something happened"
        );

        SessionId sessionId = new SessionId("#someSessionId");

        ConfirmationCode confirmationCode = new ConfirmationCode("ASDXR");

        when(sessionRepositoryMock.get(any(SessionId.class))).thenThrow(
            couldNotFindSession
        );

        assertThrows(CouldNotFindSession.class, () -> {
            sessionService.confirmSession(sessionId, confirmationCode);
        });
    }

    @Test
    @DisplayName("It should throw session not found")
    void shouldThrowSessionNotFound()
        throws SessionNotFound, CouldNotFindSession {
        SessionId sessionId = new SessionId("#someSessionId");

        SessionNotFound sessionNotFound = new SessionNotFound(sessionId);

        ConfirmationCode confirmationCode = new ConfirmationCode("ASDXR");

        when(sessionRepositoryMock.get(any(SessionId.class))).thenThrow(
            sessionNotFound
        );

        assertThrows(SessionNotFound.class, () -> {
            sessionService.confirmSession(sessionId, confirmationCode);
        });
    }

    @Test
    @DisplayName(
        "It should throw confirmation code is not equal to session confirmation code"
    )
    void shouldThrowConfirmationCodeIsNotEqualToSessionConfirmationCode()
        throws SessionNotFound, CouldNotFindSession {
        SessionId sessionId = new SessionId("#someSessionId");

        ConfirmationCode aConfirmationCode = new ConfirmationCode("ASDXR");
        ConfirmationCode sessionConfirmationCode = new ConfirmationCode(6);

        Session session = new Session(
            sessionId,
            new UserBuilder().build().getId(),
            new SessionDuration(Long.valueOf(10)),
            new SessionStatus(),
            sessionConfirmationCode
        );

        when(sessionRepositoryMock.get(any(SessionId.class))).thenReturn(
            session
        );

        assertThrows(
            ConfirmationCodeIsNotEqualToSessionConfirmationCode.class,
            () -> {
                sessionService.confirmSession(sessionId, aConfirmationCode);
            }
        );
    }

    @Test
    @DisplayName("it should confirmate the session")
    void shouldConfirmateTheSession()
        throws SessionNotFound, CouldNotFindSession {
        SessionId sessionId = new SessionId("#someSessionId");

        ConfirmationCode confirmationCode = new ConfirmationCode("ASDXR");

        Session session = new Session(
            sessionId,
            new UserBuilder().build().getId(),
            new SessionDuration(Long.valueOf(10)),
            new SessionStatus(),
            confirmationCode
        );

        when(sessionRepositoryMock.get(any(SessionId.class))).thenReturn(
            session
        );

        sessionService.confirmSession(sessionId, confirmationCode);

        assertTrue(session.isConfirmated());
    }

    @Nested
    @DisplayName("Testing SessionService.sendConfirmationCode")
    class TestSendConfirmationCode {

        @Test
        @DisplayName("should throw CouldNotFindSession")
        void shouldThrowCouldNotFindSession()
            throws SessionNotFound, CouldNotFindSession {
            SessionId sessionId = new SessionId("#someSessionId");

            CouldNotFindSession couldNotFindSession = new CouldNotFindSession(
                "Some error has occurred"
            );

            when(sessionRepositoryMock.get(any(SessionId.class))).thenThrow(
                couldNotFindSession
            );

            assertThrows(CouldNotFindSession.class, () -> {
                sessionService.sendConfirmationCode(sessionId);
            });
        }

        @Test
        @DisplayName("should throw SessionNotFound")
        void shouldThrowSessionNotFound()
            throws SessionNotFound, CouldNotFindSession {
            SessionId sessionId = new SessionId("#someSessionId");

            SessionNotFound sessionNotFound = new SessionNotFound(sessionId);

            when(sessionRepositoryMock.get(any(SessionId.class))).thenThrow(
                sessionNotFound
            );

            assertThrows(SessionNotFound.class, () -> {
                sessionService.sendConfirmationCode(sessionId);
            });
        }

        @Test
        @DisplayName("should throw UserNotFound")
        void shouldThrowUserNotFound()
            throws SessionNotFound, CouldNotFindSession, UserNotFoundException, CouldNotRetrieveUser {
            SessionId sessionId = new SessionId("#someSessionId");

            UserNotFoundException userNotFoundException =
                new UserNotFoundException("Something happened");

            when(sessionRepositoryMock.get(any(SessionId.class))).thenReturn(
                new SessionBuilder().build()
            );

            when(userRepositoryMock.getUserById(any(UserId.class))).thenThrow(
                userNotFoundException
            );

            assertThrows(UserNotFoundException.class, () -> {
                sessionService.sendConfirmationCode(sessionId);
            });
        }

        @Test
        @DisplayName("should throw CouldNotRetrieveUser")
        void shouldThrowCouldNotRetrieveUser()
            throws SessionNotFound, CouldNotFindSession, UserNotFoundException, CouldNotRetrieveUser {
            SessionId sessionId = new SessionId("#someSessionId");

            CouldNotRetrieveUser couldNotRetrieveUser =
                new CouldNotRetrieveUser("Something happened");

            when(sessionRepositoryMock.get(any(SessionId.class))).thenReturn(
                new SessionBuilder().build()
            );

            when(userRepositoryMock.getUserById(any(UserId.class))).thenThrow(
                couldNotRetrieveUser
            );

            assertThrows(CouldNotRetrieveUser.class, () -> {
                sessionService.sendConfirmationCode(sessionId);
            });
        }

        @Test
        @DisplayName("should throw CouldNotSendEmail")
        void shouldThrowCouldNotSendEmail()
            throws SessionNotFound, CouldNotFindSession, EmailNotInformed, PasswordNotInformed, IdNotInformed, UserNotFoundException, CouldNotRetrieveUser {
            SessionId sessionId = new SessionId("#someSessionId");

            CouldNotSendEmail couldNotSendEmail = new CouldNotSendEmail(
                "something happened"
            );

            when(sessionRepositoryMock.get(any(SessionId.class))).thenReturn(
                new SessionBuilder().build()
            );

            when(userRepositoryMock.getUserById(any(UserId.class))).thenReturn(
                new UserBuilder().build()
            );

            doThrow(couldNotSendEmail)
                .when(emailSenderProvider)
                .sendConfirmationCode(
                    any(ConfirmationCode.class),
                    any(User.class)
                );

            assertThrows(CouldNotSendEmail.class, () -> {
                sessionService.sendConfirmationCode(sessionId);
            });
        }

        @Test
        @DisplayName("should run with no exception")
        void shouldRunWithNoExceptions()
            throws SessionNotFound, CouldNotFindSession, EmailNotInformed, PasswordNotInformed, IdNotInformed, UserNotFoundException, CouldNotRetrieveUser {
            SessionId sessionId = new SessionId("#someSessionId");

            when(sessionRepositoryMock.get(any(SessionId.class))).thenReturn(
                new SessionBuilder().build()
            );

            when(userRepositoryMock.getUserById(any(UserId.class))).thenReturn(
                new UserBuilder().build()
            );

            doNothing()
                .when(emailSenderProvider)
                .sendConfirmationCode(
                    any(ConfirmationCode.class),
                    any(User.class)
                );

            assertAll(() -> {
                sessionService.sendConfirmationCode(sessionId);
            });
        }
    }
}

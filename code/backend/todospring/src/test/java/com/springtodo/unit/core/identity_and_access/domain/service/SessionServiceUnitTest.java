package com.springtodo.unit.core.identity_and_access.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import com.springtodo.core.identity_and_access.domain.entity.Session;
import com.springtodo.core.identity_and_access.domain.entity.User;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSaveSession;
import com.springtodo.core.identity_and_access.domain.exception.InvalidPassword;
import com.springtodo.core.identity_and_access.domain.exception.UserNotFoundException;
import com.springtodo.core.identity_and_access.domain.repository.SessionRepository;
import com.springtodo.core.identity_and_access.domain.repository.UserRepository;
import com.springtodo.core.identity_and_access.domain.service.SessionService;
import com.springtodo.core.identity_and_access.domain.value_object.UserEmail;
import com.springtodo.core.identity_and_access.domain.value_object.UserId;
import com.springtodo.core.identity_and_access.domain.value_object.UserPassword;

@ExtendWith(MockitoExtension.class)
public class SessionServiceUnitTest {

    @Mock
    private SessionRepository sessionRepositoryMock;

    @Mock
    private UserRepository userRepositoryMock;

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
}

package com.springtodo.unit.core.identity_and_access.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.springtodo.core.identity_and_access.domain.entity.Session;
import com.springtodo.core.identity_and_access.domain.entity.User;
import com.springtodo.core.identity_and_access.domain.exception.ConfirmationCodeIsNotEqualToSessionConfirmationCode;
import com.springtodo.core.identity_and_access.domain.value_object.ConfirmationCode;
import com.springtodo.core.identity_and_access.domain.value_object.SessionDuration;
import com.springtodo.core.identity_and_access.domain.value_object.SessionId;
import com.springtodo.core.identity_and_access.domain.value_object.SessionStatus;
import com.springtodo.core.identity_and_access.domain.value_object.SessionStatusConfirmated;
import com.springtodo.core.identity_and_access.domain.value_object.SessionStatusStarted;
import com.springtodo.core.identity_and_access.domain.value_object.UserEmail;
import com.springtodo.core.identity_and_access.domain.value_object.UserId;
import com.springtodo.core.identity_and_access.domain.value_object.UserPassword;

public class SessionUnitTest {

        private User user;
        private String userEmail;
        private String userPassword;
        private Long sessionDurationInSeconds;
        private int confirmationCodeSize;

        @BeforeEach
        void setUp() {
                this.userEmail = "someUserMail@email.com";
                this.sessionDurationInSeconds = Long.valueOf(3600);
                this.confirmationCodeSize = 5;

                this.user = new User(
                                new UserId(),
                                new UserEmail(userEmail),
                                new UserPassword(userPassword));
        }

        @Test
        @DisplayName("It should start a session with generated confirmation code, session id")
        public void itShouldStartASessionWithGeneratedConfirmationCodeAndSessionId() {
                Session session = Session.startUserSession(user, sessionDurationInSeconds, confirmationCodeSize);

                assertEquals(
                                ConfirmationCode.class,
                                session.getConfirmationCode().getClass());

                assertEquals(SessionDuration.class, session.getDuration().getClass());

                assertEquals(
                                SessionStatusStarted.class,
                                session.getStatus().getClass());
        }

        @Test
        @DisplayName("It should start a session from a sessionId, a UserId, A defined Duration, A status and a Confirmation")
        public void itShouldStartASessionFromASessionIdaUserIdaDurationaStatusAndaConfirmation() {
                SessionId sessionId = new SessionId();
                UserId userId = new UserId();
                SessionDuration duration = new SessionDuration(
                                sessionDurationInSeconds);
                SessionStatus status = new SessionStatusConfirmated();
                ConfirmationCode confirmationCode = new ConfirmationCode("AXTS4");

                Session session = new Session(
                                sessionId,
                                userId,
                                duration,
                                status,
                                confirmationCode);

                assertEquals(sessionId.getClass(), session.getSessionId().getClass());
                assertEquals(userId.getClass(), session.getUserId().getClass());
                assertEquals(duration.getClass(), session.getDuration().getClass());
                assertEquals(status.getClass(), session.getStatus().getClass());
                assertEquals(confirmationCode.getClass(), session.getConfirmationCode().getClass());
        }

        @Test
        @DisplayName("It should return session is not confirmed")
        public void assertSessionNotConfirmed() {
                Session session = Session.startUserSession(user, sessionDurationInSeconds, confirmationCodeSize);

                assertFalse(session.isConfirmated());
        }

        @Test
        @DisplayName("It should assure a session is confirmed")
        public void assertSessionIsConfirmed() {
                SessionId sessionId = new SessionId();
                UserId userId = new UserId();
                SessionDuration duration = new SessionDuration(
                                sessionDurationInSeconds);
                SessionStatus status = new SessionStatusConfirmated();
                ConfirmationCode confirmationCode = new ConfirmationCode("AXTS4");

                Session session = new Session(
                                sessionId,
                                userId,
                                duration,
                                status,
                                confirmationCode);

                assertTrue(session.isConfirmated());
        }

        @Test
        @DisplayName("It should not confirm the session")
        public void shouldNotConfirmTheSession() {
                SessionId sessionId = new SessionId();
                UserId userId = new UserId();
                SessionDuration duration = new SessionDuration(
                                sessionDurationInSeconds);
                SessionStatus status = new SessionStatusStarted();
                ConfirmationCode sessionConfirmationCode = new ConfirmationCode(
                                "AXTS4");
                ConfirmationCode informedConfirmationCode = new ConfirmationCode(
                                "AXTS5");

                Session session = new Session(
                                sessionId,
                                userId,
                                duration,
                                status,
                                sessionConfirmationCode);

                assertThrows(
                                ConfirmationCodeIsNotEqualToSessionConfirmationCode.class,
                                () -> {
                                        session.confirm(informedConfirmationCode);
                                });
        }

        @Test
        @DisplayName("It should confirm the session")
        public void shouldConfirmTheSession() throws ConfirmationCodeIsNotEqualToSessionConfirmationCode {
                String confirmationCode = "AXTS4";

                SessionId sessionId = new SessionId();
                UserId userId = new UserId();
                SessionDuration duration = new SessionDuration(
                                sessionDurationInSeconds);
                SessionStatus status = new SessionStatusStarted();
                ConfirmationCode sessionConfirmationCode = new ConfirmationCode(
                                confirmationCode);
                ConfirmationCode informedConfirmationCode = new ConfirmationCode(
                                confirmationCode);

                Session session = new Session(
                                sessionId,
                                userId,
                                duration,
                                status,
                                sessionConfirmationCode);

                session.confirm(informedConfirmationCode);

                assertTrue(session.isConfirmated());
        }
}

package com.springtodo.core.identity_and_access.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.springtodo.core.identity_and_access.domain.entity.Session;
import com.springtodo.core.identity_and_access.domain.entity.User;
import com.springtodo.core.identity_and_access.domain.exception.ConfirmationCodeIsNotEqualToSessionConfirmationCode;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotFindSession;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSaveSession;
import com.springtodo.core.identity_and_access.domain.exception.InvalidPassword;
import com.springtodo.core.identity_and_access.domain.exception.SessionNotFound;
import com.springtodo.core.identity_and_access.domain.exception.UserNotFoundException;
import com.springtodo.core.identity_and_access.domain.provider.EmailSenderProvider;
import com.springtodo.core.identity_and_access.domain.repository.SessionRepository;
import com.springtodo.core.identity_and_access.domain.repository.UserRepository;
import com.springtodo.core.identity_and_access.domain.value_object.ConfirmationCode;
import com.springtodo.core.identity_and_access.domain.value_object.SessionId;
import com.springtodo.core.identity_and_access.domain.value_object.UserEmail;
import com.springtodo.core.identity_and_access.domain.value_object.UserPassword;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SessionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private EmailSenderProvider emailSenderProvider;

    @Value("${sessionDurationInSeconds}")
    private Long sessionDurationInSeconds;

    @Value("${confirmationCodeSize}")
    private int confirmationCodeSize;

    public SessionService(UserRepository userRepository) {}

    public Session startSession(
        UserPassword anUserPassword,
        UserEmail anUserEmail
    )
        throws UserNotFoundException, CouldNotRetrieveUser, InvalidPassword, CouldNotSaveSession {
        User user = this.userRepository.getUserByEmail(anUserEmail);

        if (!user.passwordEquals(anUserPassword)) {
            log.error("Invalid passwords! {}, ", anUserPassword, anUserEmail);

            throw new InvalidPassword("user password doesn't match");
        }

        Session session = new Session(
            user,
            this.sessionDurationInSeconds,
            this.confirmationCodeSize
        );

        this.sessionRepository.save(session);

        return session;
    }

    public void confirmSession(
        SessionId aSessionId,
        ConfirmationCode aConfirmationCode
    )
        throws SessionNotFound, CouldNotFindSession, ConfirmationCodeIsNotEqualToSessionConfirmationCode {
        Session session = this.sessionRepository.get(aSessionId);

        session.confirm(aConfirmationCode);
    }

    public void sendConfirmationCode(SessionId sessionId)
        throws SessionNotFound, CouldNotFindSession, UserNotFoundException, CouldNotRetrieveUser {
        Session session = this.sessionRepository.get(sessionId);

        User user = this.userRepository.getUserById(session.getUserId());

        this.emailSenderProvider.sendConfirmationCode(
            session.getConfirmationCode(),
            user
        );
    }
}

package com.springtodo.core.identity_and_access.application.usecase;

import com.springtodo.core.identity_and_access.application.dto.StartSessionInput;
import com.springtodo.core.identity_and_access.application.dto.StartSessionOutput;
import com.springtodo.core.identity_and_access.application.exception.CouldNotGenerateToken;
import com.springtodo.core.identity_and_access.application.utils.SessionTokenGeneratorUtil;
import com.springtodo.core.identity_and_access.domain.entity.Session;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSaveSession;
import com.springtodo.core.identity_and_access.domain.exception.InvalidPassword;
import com.springtodo.core.identity_and_access.domain.exception.UserNotFoundException;
import com.springtodo.core.identity_and_access.domain.service.SessionService;
import com.springtodo.core.identity_and_access.domain.value_object.UserEmail;
import com.springtodo.core.identity_and_access.domain.value_object.UserPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StartSession {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionTokenGeneratorUtil sessionTokenGeneratorUtil;

    public StartSessionOutput execute(StartSessionInput startSessionInput)
        throws UserNotFoundException, CouldNotRetrieveUser, InvalidPassword, CouldNotSaveSession, CouldNotGenerateToken {
        UserPassword anUserPassword = new UserPassword(
            startSessionInput.getPassword()
        );
        UserEmail anUserEmail = new UserEmail(startSessionInput.getEmail());

        Session session = sessionService.startSession(
            anUserPassword,
            anUserEmail
        );

        String sessionToken = sessionTokenGeneratorUtil.generate(
            session.getSessionId().getId(),
            session.getDuration().getStart(),
            session.getDuration().getEnd()
        );

        return new StartSessionOutput(sessionToken);
    }
}

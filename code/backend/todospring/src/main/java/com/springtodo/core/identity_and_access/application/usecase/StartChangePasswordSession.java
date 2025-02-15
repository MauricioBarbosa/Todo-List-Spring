package com.springtodo.core.identity_and_access.application.usecase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springtodo.core.identity_and_access.application.dto.StartChangePasswordSessionInput;
import com.springtodo.core.identity_and_access.application.dto.StartChangePasswordSessionOutput;
import com.springtodo.core.identity_and_access.application.exception.CouldNotGenerateToken;
import com.springtodo.core.identity_and_access.application.utils.SessionTokenGeneratorUtil;
import com.springtodo.core.identity_and_access.domain.entity.Session;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSaveSession;
import com.springtodo.core.identity_and_access.domain.exception.UserNotFoundException;
import com.springtodo.core.identity_and_access.domain.service.SessionService;
import com.springtodo.core.identity_and_access.domain.value_object.UserEmail;

@Service
public class StartChangePasswordSession {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionTokenGeneratorUtil sessionTokenGeneratorUtil;

    public StartChangePasswordSessionOutput execute(StartChangePasswordSessionInput startChangePasswordSessionInput)
            throws UserNotFoundException, CouldNotRetrieveUser, CouldNotSaveSession, CouldNotGenerateToken {
        UserEmail userEmail = new UserEmail(startChangePasswordSessionInput.getUserEmail());

        Session session = sessionService.startChangePasswordSession(userEmail);

        String sessionToken = sessionTokenGeneratorUtil.generate(
                session.getSessionId().getId(),
                session.getDuration().getStart(),
                session.getDuration().getEnd());

        StartChangePasswordSessionOutput startChangePasswordSessionOutput = new StartChangePasswordSessionOutput();

        startChangePasswordSessionOutput.setSessionToken(sessionToken);

        return startChangePasswordSessionOutput;
    }
}

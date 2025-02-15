package com.springtodo.core.identity_and_access.application.usecase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springtodo.core.identity_and_access.application.dto.StartUserSessionInput;
import com.springtodo.core.identity_and_access.application.dto.StartUserSessionOutput;
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

@Service
public class StartUserSession {

        @Autowired
        private SessionService sessionService;

        @Autowired
        private SessionTokenGeneratorUtil sessionTokenGeneratorUtil;

        public StartUserSessionOutput execute(StartUserSessionInput startSessionInput)
                        throws UserNotFoundException, CouldNotRetrieveUser, InvalidPassword, CouldNotSaveSession,
                        CouldNotGenerateToken {
                UserPassword anUserPassword = new UserPassword(
                                startSessionInput.getPassword());
                UserEmail anUserEmail = new UserEmail(startSessionInput.getEmail());

                Session session = sessionService.startUserSession(
                                anUserPassword,
                                anUserEmail);

                String sessionToken = sessionTokenGeneratorUtil.generate(
                                session.getSessionId().getId(),
                                session.getDuration().getStart(),
                                session.getDuration().getEnd());

                StartUserSessionOutput startUserSessionOutput = new StartUserSessionOutput();

                startUserSessionOutput.setSessionToken(sessionToken);

                return startUserSessionOutput;
        }
}

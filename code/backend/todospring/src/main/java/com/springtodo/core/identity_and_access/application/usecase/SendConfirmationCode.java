package com.springtodo.core.identity_and_access.application.usecase;

import com.springtodo.core.identity_and_access.application.dto.SendConfirmationCodeInput;
import com.springtodo.core.identity_and_access.application.exception.CouldNotDecodeToken;
import com.springtodo.core.identity_and_access.application.exception.InvalidToken;
import com.springtodo.core.identity_and_access.application.utils.SessionTokenGeneratorUtil;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotFindSession;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSendEmail;
import com.springtodo.core.identity_and_access.domain.exception.SessionNotFound;
import com.springtodo.core.identity_and_access.domain.exception.UserNotFoundException;
import com.springtodo.core.identity_and_access.domain.service.SessionService;
import com.springtodo.core.identity_and_access.domain.value_object.SessionId;
import org.springframework.beans.factory.annotation.Autowired;

public class SendConfirmationCode {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionTokenGeneratorUtil sessionTokenGeneratorUtil;

    public void execute(SendConfirmationCodeInput sendConfirmationCodeInput)
        throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession, UserNotFoundException, CouldNotRetrieveUser, CouldNotSendEmail {
        SessionId sessionId = sessionTokenGeneratorUtil.decode(
            sendConfirmationCodeInput.getSessionToken()
        );

        sessionService.sendConfirmationCode(sessionId);
    }
}

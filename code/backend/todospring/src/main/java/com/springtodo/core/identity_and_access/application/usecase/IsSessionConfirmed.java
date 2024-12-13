package com.springtodo.core.identity_and_access.application.usecase;

import com.springtodo.core.identity_and_access.application.dto.IsSessionConfirmatedInput;
import com.springtodo.core.identity_and_access.application.exception.CouldNotDecodeToken;
import com.springtodo.core.identity_and_access.application.exception.InvalidToken;
import com.springtodo.core.identity_and_access.application.utils.SessionTokenGeneratorUtil;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotFindSession;
import com.springtodo.core.identity_and_access.domain.exception.SessionNotFound;
import com.springtodo.core.identity_and_access.domain.service.SessionService;
import com.springtodo.core.identity_and_access.domain.value_object.SessionId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IsSessionConfirmed {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionTokenGeneratorUtil sessionTokenGeneratorUtil;

    public boolean execute(IsSessionConfirmatedInput isSessionConfirmatedInput)
        throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession {
        SessionId sessionId = sessionTokenGeneratorUtil.decode(
            isSessionConfirmatedInput.getSessionToken()
        );

        return sessionService.isSessionConfirmed(sessionId);
    }
}

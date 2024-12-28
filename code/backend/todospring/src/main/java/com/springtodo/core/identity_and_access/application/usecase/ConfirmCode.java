package com.springtodo.core.identity_and_access.application.usecase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springtodo.core.identity_and_access.application.dto.ConfirmCodeInput;
import com.springtodo.core.identity_and_access.application.exception.CouldNotDecodeToken;
import com.springtodo.core.identity_and_access.application.exception.InvalidToken;
import com.springtodo.core.identity_and_access.application.utils.SessionTokenGeneratorUtil;
import com.springtodo.core.identity_and_access.domain.exception.ConfirmationCodeIsNotEqualToSessionConfirmationCode;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotFindSession;
import com.springtodo.core.identity_and_access.domain.exception.SessionNotFound;
import com.springtodo.core.identity_and_access.domain.service.SessionService;
import com.springtodo.core.identity_and_access.domain.value_object.ConfirmationCode;
import com.springtodo.core.identity_and_access.domain.value_object.SessionId;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConfirmCode {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionTokenGeneratorUtil sessionTokenGeneratorUtil;

    public void execute(ConfirmCodeInput confirmCodeInput)
            throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession,
            ConfirmationCodeIsNotEqualToSessionConfirmationCode {
        SessionId sessionId = sessionTokenGeneratorUtil.decode(
                confirmCodeInput.getSessionToken());

        ConfirmationCode confirmationCode = new ConfirmationCode(
                confirmCodeInput.getConfirmationCode());

        log.info("confirming session {}", sessionId);

        this.sessionService.confirmSession(sessionId, confirmationCode);

        log.info("session confirmated! {}", sessionId);
    }
}

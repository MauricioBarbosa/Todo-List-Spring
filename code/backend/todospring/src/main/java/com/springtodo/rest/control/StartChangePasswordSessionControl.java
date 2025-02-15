package com.springtodo.rest.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springtodo.core.identity_and_access.application.dto.StartChangePasswordSessionInput;
import com.springtodo.core.identity_and_access.application.dto.StartChangePasswordSessionOutput;
import com.springtodo.core.identity_and_access.application.exception.CouldNotGenerateToken;
import com.springtodo.core.identity_and_access.application.usecase.StartChangePasswordSession;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSaveSession;
import com.springtodo.core.identity_and_access.domain.exception.UserNotFoundException;
import com.springtodo.rest.pojo.start_change_password_session_control.StartChangePasswordSessionRequestBody;
import com.springtodo.rest.pojo.start_change_password_session_control.StartChangePasswordSessionResponse;

import jakarta.validation.Valid;

@RestController
public class StartChangePasswordSessionControl {

    @Autowired
    private StartChangePasswordSession startChangePasswordSession;

    @PostMapping("/create-change-password-session")
    public ResponseEntity<StartChangePasswordSessionResponse> startChangePasswordSession(
            @Valid @RequestBody StartChangePasswordSessionRequestBody startChangePasswordRequestBody)
            throws UserNotFoundException, CouldNotRetrieveUser, CouldNotSaveSession, CouldNotGenerateToken {

        StartChangePasswordSessionInput startChangePasswordSessionInput = new StartChangePasswordSessionInput();

        startChangePasswordSessionInput.setUserEmail(startChangePasswordRequestBody.getUserEmail());

        StartChangePasswordSessionOutput startChangePasswordSessionOutput = startChangePasswordSession
                .execute(startChangePasswordSessionInput);

        StartChangePasswordSessionResponse startChangePasswordSessionResponse = new StartChangePasswordSessionResponse();

        startChangePasswordSessionResponse.setSessionToken(startChangePasswordSessionOutput.getSessionToken());

        return ResponseEntity.status(HttpStatus.OK).body(startChangePasswordSessionResponse);
    }
}

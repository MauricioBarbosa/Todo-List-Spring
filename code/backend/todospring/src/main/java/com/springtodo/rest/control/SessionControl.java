package com.springtodo.rest.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.springtodo.core.identity_and_access.application.dto.ConfirmCodeInput;
import com.springtodo.core.identity_and_access.application.dto.StartSessionInput;
import com.springtodo.core.identity_and_access.application.dto.StartSessionOutput;
import com.springtodo.core.identity_and_access.application.exception.CouldNotDecodeToken;
import com.springtodo.core.identity_and_access.application.exception.CouldNotGenerateToken;
import com.springtodo.core.identity_and_access.application.exception.InvalidToken;
import com.springtodo.core.identity_and_access.application.usecase.ConfirmCode;
import com.springtodo.core.identity_and_access.application.usecase.StartSession;
import com.springtodo.core.identity_and_access.domain.exception.ConfirmationCodeIsNotEqualToSessionConfirmationCode;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotFindSession;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSaveSession;
import com.springtodo.core.identity_and_access.domain.exception.InvalidPassword;
import com.springtodo.core.identity_and_access.domain.exception.SessionNotFound;
import com.springtodo.core.identity_and_access.domain.exception.UserNotFoundException;
import com.springtodo.rest.pojo.SessionControl.ConfirmSessionInput;
import com.springtodo.rest.pojo.SessionControl.LoginInput;
import com.springtodo.rest.pojo.SessionControl.LoginOutput;

import jakarta.validation.Valid;

@RestController
public class SessionControl {

    @Autowired
    private StartSession startSessionUseCase;

    @Autowired
    private ConfirmCode confirmCodeUseCase;

    @PostMapping("/login")
    public ResponseEntity<LoginOutput> login(
        @Valid @RequestBody LoginInput loginInput
    )
        throws UserNotFoundException, CouldNotRetrieveUser, InvalidPassword, CouldNotSaveSession, CouldNotGenerateToken {
        StartSessionOutput startSessionOutput = startSessionUseCase.execute(
            new StartSessionInput(
                loginInput.getEmail(),
                loginInput.getPassword()
            )
        );
        return ResponseEntity.status(HttpStatus.OK).body(
            new LoginOutput(startSessionOutput.getSessionToken())
        );
    }

    @PostMapping("/confirm-session")
    public ResponseEntity<Void> confirmSession(
        @RequestHeader String sessionToken,
        @Valid @RequestBody ConfirmSessionInput confirmSessionInput
    ) throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession, ConfirmationCodeIsNotEqualToSessionConfirmationCode {
        confirmCodeUseCase.execute(
            new ConfirmCodeInput(
                confirmSessionInput.getConfirmationCode(),
                sessionToken
            )
        );

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

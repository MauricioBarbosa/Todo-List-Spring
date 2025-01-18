package com.springtodo.rest.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.springtodo.core.identity_and_access.application.dto.ChangePasswordInput;
import com.springtodo.core.identity_and_access.application.exception.CouldNotDecodeToken;
import com.springtodo.core.identity_and_access.application.exception.InvalidToken;
import com.springtodo.core.identity_and_access.application.usecase.ChangePassword;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotFindSession;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSaveUser;
import com.springtodo.core.identity_and_access.domain.exception.InvalidPassword;
import com.springtodo.core.identity_and_access.domain.exception.NewPasswordShouldNotEqualsToPreviousPassword;
import com.springtodo.core.identity_and_access.domain.exception.OldPasswordDoesNotEqualToUserPassword;
import com.springtodo.core.identity_and_access.domain.exception.SessionNotFound;
import com.springtodo.core.identity_and_access.domain.exception.UserNotFoundException;
import com.springtodo.rest.pojo.change_password_control.ChangePasswordRequestBody;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ChangePasswordControl {

    @Autowired
    private ChangePassword changePasswordUseCase;

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestHeader String sessionToken,
            @Valid @RequestBody ChangePasswordRequestBody changePasswordRequestBody)
            throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession, UserNotFoundException,
            CouldNotRetrieveUser, InvalidPassword, OldPasswordDoesNotEqualToUserPassword,
            NewPasswordShouldNotEqualsToPreviousPassword, CouldNotSaveUser {

        ChangePasswordInput changePasswordInput = new ChangePasswordInput();

        changePasswordInput.setNewPassword(changePasswordRequestBody.getNewPassword());
        changePasswordInput.setOldPassword(changePasswordRequestBody.getOldPassword());
        changePasswordInput.setSessionToken(sessionToken);

        changePasswordUseCase.execute(changePasswordInput);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

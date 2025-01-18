package com.springtodo.rest.exception_handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.springtodo.core.identity_and_access.application.exception.CouldNotDecodeToken;
import com.springtodo.core.identity_and_access.application.exception.InvalidToken;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotFindSession;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSaveUser;
import com.springtodo.core.identity_and_access.domain.exception.InvalidPassword;
import com.springtodo.core.identity_and_access.domain.exception.NewPasswordShouldNotEqualsToPreviousPassword;
import com.springtodo.core.identity_and_access.domain.exception.OldPasswordDoesNotEqualToUserPassword;
import com.springtodo.core.identity_and_access.domain.exception.SessionNotFound;
import com.springtodo.core.identity_and_access.domain.exception.UserNotFoundException;
import com.springtodo.rest.pojo.shared.BadRequestOutput;
import com.springtodo.rest.pojo.shared.UnauthorizedOutput;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChangePasswordControlExceptionHandler {

    @ExceptionHandler({ InvalidToken.class })
    public ResponseEntity<UnauthorizedOutput> handleInvalidToken(InvalidToken invalidPassword) {
        UnauthorizedOutput unauthorizedOutput = new UnauthorizedOutput();

        unauthorizedOutput.setMessage(invalidPassword.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                unauthorizedOutput);
    }

    @ExceptionHandler({ SessionNotFound.class })
    public ResponseEntity<UnauthorizedOutput> handleSessionNotFound(SessionNotFound sessionNotFound) {
        UnauthorizedOutput unauthorizedOutput = new UnauthorizedOutput();

        unauthorizedOutput.setMessage(sessionNotFound.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                unauthorizedOutput);
    }

    @ExceptionHandler({ CouldNotFindSession.class })
    public ResponseEntity<Void> handleCouldNotFindSession(CouldNotFindSession couldNotFindSession) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler({ CouldNotDecodeToken.class })
    public ResponseEntity<Void> handleCouldNotDecodeToken(CouldNotDecodeToken couldNotDecodeToken) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler({ UserNotFoundException.class })
    public ResponseEntity<Void> handleUserNotFoundException(UserNotFoundException userNotFoundException) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler({ CouldNotRetrieveUser.class })
    public ResponseEntity<Void> handleCouldNotRetrieveUser(CouldNotRetrieveUser couldNotRetrieveUser) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler({ InvalidPassword.class })
    public ResponseEntity<BadRequestOutput> handleInvalidPassword(InvalidPassword invalidPassword) {
        BadRequestOutput badRequestOutput = new BadRequestOutput();

        badRequestOutput.setMessage(invalidPassword.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                badRequestOutput);
    }

    @ExceptionHandler({ OldPasswordDoesNotEqualToUserPassword.class })
    public ResponseEntity<BadRequestOutput> handleOldPasswordDoesNotEqualToUserPassword(
            OldPasswordDoesNotEqualToUserPassword oldPasswordDoesNotEqualToUserPassword) {
        BadRequestOutput badRequestOutput = new BadRequestOutput();

        badRequestOutput.setMessage(oldPasswordDoesNotEqualToUserPassword.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                badRequestOutput);
    }

    @ExceptionHandler({ NewPasswordShouldNotEqualsToPreviousPassword.class })
    public ResponseEntity<BadRequestOutput> handleNewPasswordShouldNotEqualsToPreviousPassword(
            NewPasswordShouldNotEqualsToPreviousPassword newPasswordShouldNotEqualsToPreviousPassword) {
        BadRequestOutput badRequestOutput = new BadRequestOutput();

        badRequestOutput.setMessage(newPasswordShouldNotEqualsToPreviousPassword.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                badRequestOutput);
    }

    @ExceptionHandler({ CouldNotSaveUser.class })
    public ResponseEntity<Void> handleCouldNotSaveUser(
            CouldNotSaveUser couldNotSaveUser) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

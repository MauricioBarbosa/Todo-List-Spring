package com.springtodo.rest.exception_handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.springtodo.core.identity_and_access.application.exception.CouldNotDecodeToken;
import com.springtodo.core.identity_and_access.application.exception.CouldNotGenerateToken;
import com.springtodo.core.identity_and_access.application.exception.InvalidToken;
import com.springtodo.core.identity_and_access.domain.exception.ConfirmationCodeIsNotEqualToSessionConfirmationCode;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotFindSession;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSaveSession;
import com.springtodo.core.identity_and_access.domain.exception.InvalidPassword;
import com.springtodo.core.identity_and_access.domain.exception.SessionNotFound;
import com.springtodo.core.identity_and_access.domain.exception.UserNotFoundException;
import com.springtodo.rest.pojo.shared.BadRequestOutput;
import com.springtodo.rest.pojo.shared.ForbiddenOutput;
import com.springtodo.rest.pojo.shared.UnauthorizedOutput;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class SessionControlExceptionHandler {

    @ExceptionHandler({ InvalidPassword.class })
    public ResponseEntity<BadRequestOutput> handleInvalidPassword(
            InvalidPassword invalidPassword) {
        return buildBadRequestResponse(invalidPassword);
    }

    @ExceptionHandler({ UserNotFoundException.class })
    public ResponseEntity<BadRequestOutput> handleUserNotFoundException(
            UserNotFoundException userNotFoundException) {
        return buildBadRequestResponse(userNotFoundException);
    }

    @ExceptionHandler({ CouldNotRetrieveUser.class })
    public ResponseEntity<Void> handleCouldNotRetrieveUser(
            CouldNotRetrieveUser couldNotRetrieveUser) {
        return buildInternalServerErrorResponse();
    }

    @ExceptionHandler({ CouldNotSaveSession.class })
    public ResponseEntity<Void> handleCouldNotSaveSession(
            CouldNotSaveSession couldNotSaveSession) {
        return buildInternalServerErrorResponse();
    }

    @ExceptionHandler({ CouldNotGenerateToken.class })
    public ResponseEntity<Void> handleCouldNotGenerateToken(
            CouldNotGenerateToken couldNotGenerateToken) {
        return buildInternalServerErrorResponse();
    }

    @ExceptionHandler({ CouldNotDecodeToken.class })
    public ResponseEntity<Void> handleCouldNotDecodeToken(
            CouldNotDecodeToken couldNotDecodeToken) {
        return buildInternalServerErrorResponse();
    }

    @ExceptionHandler({ SessionNotFound.class })
    public ResponseEntity<UnauthorizedOutput> handleSessionNotFound(
            SessionNotFound sessionNotFound) {
        return buildUnauthorizedResponse(sessionNotFound);
    }

    @ExceptionHandler({ CouldNotFindSession.class })
    public ResponseEntity<Void> handleCouldNotFindSession(
            CouldNotFindSession couldNotFindSession) {
        return buildInternalServerErrorResponse();
    }

    @ExceptionHandler({ ConfirmationCodeIsNotEqualToSessionConfirmationCode.class })
    public ResponseEntity<BadRequestOutput> handleConfirmationCodeIsNotEqualToSessionConfirmationCode(
            ConfirmationCodeIsNotEqualToSessionConfirmationCode confirmationCodeIsNotEqualToSessionConfirmationCode) {
        return buildBadRequestResponse(
                confirmationCodeIsNotEqualToSessionConfirmationCode);
    }

    @ExceptionHandler({ InvalidToken.class })
    public ResponseEntity<ForbiddenOutput> handleInvalidToken(
            InvalidToken invalidToken) {
        return buildForbiddenResponse(invalidToken);
    }

    private ResponseEntity<BadRequestOutput> buildBadRequestResponse(
            Exception exception) {
        BadRequestOutput badRequestOutput = new BadRequestOutput();

        badRequestOutput.setMessage(exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                badRequestOutput);
    }

    private ResponseEntity<UnauthorizedOutput> buildUnauthorizedResponse(
            Exception exception) {

        log.info("returning unauthorized body {}", exception);

        UnauthorizedOutput unauthorizedOutput = new UnauthorizedOutput();

        unauthorizedOutput.setMessage(exception.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                unauthorizedOutput);
    }

    private ResponseEntity<ForbiddenOutput> buildForbiddenResponse(Exception exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    private ResponseEntity<Void> buildInternalServerErrorResponse() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

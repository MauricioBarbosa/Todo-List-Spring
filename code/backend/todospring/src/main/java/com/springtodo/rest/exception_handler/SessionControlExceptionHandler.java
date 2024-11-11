package com.springtodo.rest.exception_handler;

import com.springtodo.core.identity_and_access.application.exception.CouldNotDecodeToken;
import com.springtodo.core.identity_and_access.application.exception.CouldNotGenerateToken;
import com.springtodo.core.identity_and_access.domain.exception.ConfirmationCodeIsNotEqualToSessionConfirmationCode;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotFindSession;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSaveSession;
import com.springtodo.core.identity_and_access.domain.exception.InvalidPassword;
import com.springtodo.core.identity_and_access.domain.exception.SessionNotFound;
import com.springtodo.core.identity_and_access.domain.exception.UserNotFoundException;
import com.springtodo.rest.pojo.shared.BadRequestOutput;
import com.springtodo.rest.pojo.shared.UnauthorizedOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class SessionControlExceptionHandler {

    public ResponseEntity<BadRequestOutput> handleInvalidPassword(
        InvalidPassword invalidPassword
    ) {
        return buildBadRequestResponse(invalidPassword);
    }

    public ResponseEntity<BadRequestOutput> handleUserNotFoundException(
        UserNotFoundException userNotFoundException
    ) {
        return buildBadRequestResponse(userNotFoundException);
    }

    public ResponseEntity<Void> handleCouldNotRetrieveUser(
        CouldNotRetrieveUser couldNotRetrieveUser
    ) {
        return buildInternalServerErrorResponse();
    }

    public ResponseEntity<Void> handleCouldNotSaveSession(
        CouldNotSaveSession couldNotSaveSession
    ) {
        return buildInternalServerErrorResponse();
    }

    public ResponseEntity<Void> handleCouldNotGenerateToken(
        CouldNotGenerateToken couldNotGenerateToken
    ) {
        return buildInternalServerErrorResponse();
    }

    public ResponseEntity<Void> handleCouldNotDecodeToken(
        CouldNotDecodeToken couldNotDecodeToken
    ) {
        return buildInternalServerErrorResponse();
    }

    public ResponseEntity<UnauthorizedOutput> handleSessionNotFound(
        SessionNotFound sessionNotFound
    ) {
        return buildUnauthorizedResponse(sessionNotFound);
    }

    public ResponseEntity<Void> handleCouldNotFindSession(
        CouldNotFindSession couldNotFindSession
    ) {
        return buildInternalServerErrorResponse();
    }

    public ResponseEntity<
        BadRequestOutput
    > handleConfirmationCodeIsNotEqualToSessionConfirmationCode(
        ConfirmationCodeIsNotEqualToSessionConfirmationCode confirmationCodeIsNotEqualToSessionConfirmationCode
    ) {
        return buildBadRequestResponse(
            confirmationCodeIsNotEqualToSessionConfirmationCode
        );
    }

    private ResponseEntity<BadRequestOutput> buildBadRequestResponse(
        Exception exception
    ) {
        BadRequestOutput badRequestOutput = new BadRequestOutput();

        badRequestOutput.setMessage(exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            badRequestOutput
        );
    }

    private ResponseEntity<UnauthorizedOutput> buildUnauthorizedResponse(
        Exception exception
    ) {
        UnauthorizedOutput unauthorizedOutput = new UnauthorizedOutput();

        unauthorizedOutput.setMessage(exception.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            unauthorizedOutput
        );
    }

    private ResponseEntity<Void> buildInternalServerErrorResponse() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

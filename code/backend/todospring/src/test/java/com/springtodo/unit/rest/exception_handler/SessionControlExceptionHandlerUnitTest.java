package com.springtodo.unit.rest.exception_handler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.springtodo.core.identity_and_access.application.exception.CouldNotDecodeToken;
import com.springtodo.core.identity_and_access.application.exception.CouldNotGenerateToken;
import com.springtodo.core.identity_and_access.domain.exception.ConfirmationCodeIsNotEqualToSessionConfirmationCode;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotFindSession;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSaveSession;
import com.springtodo.core.identity_and_access.domain.exception.InvalidPassword;
import com.springtodo.core.identity_and_access.domain.exception.SessionNotFound;
import com.springtodo.core.identity_and_access.domain.exception.UserNotFoundException;
import com.springtodo.core.identity_and_access.domain.value_object.SessionId;
import com.springtodo.rest.exception_handler.SessionControlExceptionHandler;
import com.springtodo.rest.pojo.shared.BadRequestOutput;
import com.springtodo.rest.pojo.shared.UnauthorizedOutput;

public class SessionControlExceptionHandlerUnitTest {

        private SessionControlExceptionHandler sessionControlExceptionHandler;

        @BeforeEach
        void setUp() {
                this.sessionControlExceptionHandler = new SessionControlExceptionHandler();
        }

        @Test
        void handleInvalidPasswordException_shouldReturnBadRequestWithMessage() {
                InvalidPassword invalidPassword = new InvalidPassword(
                                "some invalid password error occurrs");

                ResponseEntity<BadRequestOutput> response = sessionControlExceptionHandler.handleInvalidPassword(
                                invalidPassword);

                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals(
                                response.getBody().getMessages().get(0),
                                invalidPassword.getMessage());
        }

        @Test
        void handleUserNotFoundException_shouldReturnBadRequestWithMessage() {
                UserNotFoundException userNotFoundException = new UserNotFoundException(
                                "user not found");

                ResponseEntity<BadRequestOutput> response = sessionControlExceptionHandler.handleUserNotFoundException(
                                userNotFoundException);

                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals(
                                response.getBody().getMessages().get(0),
                                userNotFoundException.getMessage());
        }

        @Test
        void handleCouldNotRetrieveUser_shouldReturnInternalServerError() {
                CouldNotRetrieveUser couldNotRetrieveUser = new CouldNotRetrieveUser(
                                "could not retrieve user");

                ResponseEntity<Void> response = sessionControlExceptionHandler.handleCouldNotRetrieveUser(
                                couldNotRetrieveUser);

                assertEquals(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                response.getStatusCode());
                assertEquals(response.getBody(), null);
        }

        @Test
        void handleCouldNotSaveSession_shouldReturnInternalServerError() {
                CouldNotSaveSession couldNotSaveSession = new CouldNotSaveSession(
                                "Could not save session for some reason");

                ResponseEntity<Void> response = sessionControlExceptionHandler.handleCouldNotSaveSession(
                                couldNotSaveSession);

                assertEquals(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                response.getStatusCode());
                assertEquals(response.getBody(), null);
        }

        @Test
        void handleCouldNotGenerateToken_shouldReturnInternalServerError() {
                CouldNotGenerateToken couldNotGenerateToken = new CouldNotGenerateToken(
                                "could not generate token for some reason");

                ResponseEntity<Void> response = sessionControlExceptionHandler.handleCouldNotGenerateToken(
                                couldNotGenerateToken);

                assertEquals(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                response.getStatusCode());
                assertEquals(response.getBody(), null);
        }

        @Test
        void handleCouldNotDecodeToken_shouldReturnInternalServerError() {
                CouldNotDecodeToken couldNotDecodeToken = new CouldNotDecodeToken(
                                "could not decode token for some reason");

                ResponseEntity<Void> response = sessionControlExceptionHandler.handleCouldNotDecodeToken(
                                couldNotDecodeToken);

                assertEquals(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                response.getStatusCode());
                assertEquals(response.getBody(), null);
        }

        void handleSessionNotFound_shouldReturnInternalServerError() {
                SessionId sessionId = new SessionId("someSessionId");

                SessionNotFound sessionNotFound = new SessionNotFound(sessionId);

                ResponseEntity<UnauthorizedOutput> response = sessionControlExceptionHandler.handleSessionNotFound(
                                sessionNotFound);

                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals(
                                response.getBody().getMessages().get(0),
                                sessionNotFound.getMessage());
        }

        @Test
        void couldNotFindSession_shouldReturnInternalServerError() {
                CouldNotFindSession couldNotFindSession = new CouldNotFindSession(
                                "Could not find session for some readon");

                ResponseEntity<Void> response = sessionControlExceptionHandler.handleCouldNotFindSession(
                                couldNotFindSession);

                assertEquals(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                response.getStatusCode());
                assertEquals(response.getBody(), null);
        }

        @Test
        void couldConfirmationCodeIsNotEqualToSessionConfirmationCode_shouldReturnBadRequestWithMessage() {
                ConfirmationCodeIsNotEqualToSessionConfirmationCode confirmationCodeIsNotEqualToSessionConfirmationCode = new ConfirmationCodeIsNotEqualToSessionConfirmationCode(
                                "confirmation code is invalid");

                ResponseEntity<BadRequestOutput> response = sessionControlExceptionHandler
                                .handleConfirmationCodeIsNotEqualToSessionConfirmationCode(
                                                confirmationCodeIsNotEqualToSessionConfirmationCode);

                assertEquals(
                                HttpStatus.BAD_REQUEST,
                                response.getStatusCode());
                assertEquals(
                                response.getBody().getMessages().get(0),
                                confirmationCodeIsNotEqualToSessionConfirmationCode.getMessage());
        }
}

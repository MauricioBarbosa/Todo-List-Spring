package com.springtodo.unit.rest.exception_handler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
import com.springtodo.core.identity_and_access.domain.value_object.SessionId;
import com.springtodo.rest.exception_handler.ChangePasswordControlExceptionHandler;
import com.springtodo.rest.pojo.shared.BadRequestOutput;
import com.springtodo.rest.pojo.shared.UnauthorizedOutput;

public class ChangePasswordControlExceptionHandlerUnitTest {
        private ChangePasswordControlExceptionHandler changePasswordControlExceptionHandler;

        @BeforeEach
        void setUp() {
                this.changePasswordControlExceptionHandler = new ChangePasswordControlExceptionHandler();
        }

        @Test
        void it_shouldReturn401WhenHandlingWhenInvalidToken() {
                InvalidToken invalidToken = new InvalidToken();

                ResponseEntity<UnauthorizedOutput> response = changePasswordControlExceptionHandler.handleInvalidToken(
                                invalidToken);

                assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
                assertEquals(
                                response.getBody().getMessages().get(0),
                                invalidToken.getMessage());
        }

        @Test
        void it_shouldReturn401WhenHandlingWhenSessionNotFound() {
                SessionNotFound sessionNotFound = new SessionNotFound(new SessionId());

                ResponseEntity<UnauthorizedOutput> response = changePasswordControlExceptionHandler
                                .handleSessionNotFound(
                                                sessionNotFound);

                assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
                assertEquals(
                                response.getBody().getMessages().get(0),
                                sessionNotFound.getMessage());
        }

        @Test
        void it_shouldReturn500WhenHandlingWithCouldNotDecodeToken() {
                CouldNotDecodeToken couldNotDecodeToken = new CouldNotDecodeToken("someError");

                ResponseEntity<Void> response = changePasswordControlExceptionHandler.handleCouldNotDecodeToken(
                                couldNotDecodeToken);

                assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }

        @Test
        void it_shouldReturn500WhenHandlingWithCouldNotFindSession() {
                CouldNotFindSession couldNotFindSession = new CouldNotFindSession("some error has occurred");

                ResponseEntity<Void> response = changePasswordControlExceptionHandler
                                .handleCouldNotFindSession(couldNotFindSession);

                assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }

        @Test
        void it_shouldReturn500WhenHandlingWithUserNotFound() {
                UserNotFoundException userNotFoundException = new UserNotFoundException("some error has occurred");

                ResponseEntity<Void> response = changePasswordControlExceptionHandler
                                .handleUserNotFoundException(userNotFoundException);

                assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }

        @Test
        void it_shouldReturn500WhenCouldNotRetrieveUser() {
                CouldNotRetrieveUser couldNotRetrieveUser = new CouldNotRetrieveUser("some error has occurred");

                ResponseEntity<Void> response = changePasswordControlExceptionHandler
                                .handleCouldNotRetrieveUser(couldNotRetrieveUser);

                assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }

        @Test
        void it_shouldReturn400WhenInvalidPassword() {
                InvalidPassword invalidPassword = new InvalidPassword("some error message");

                ResponseEntity<BadRequestOutput> response = changePasswordControlExceptionHandler
                                .handleInvalidPassword(invalidPassword);

                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals(
                                response.getBody().getMessages().get(0),
                                invalidPassword.getMessage());
        }

        @Test
        void it_shouldReturn400WhenOldPasswordDoesNotEqualToUserPassword() {
                OldPasswordDoesNotEqualToUserPassword oldPasswordDoesNotEqualToUserPassword = new OldPasswordDoesNotEqualToUserPassword();

                ResponseEntity<BadRequestOutput> response = changePasswordControlExceptionHandler
                                .handleOldPasswordDoesNotEqualToUserPassword(oldPasswordDoesNotEqualToUserPassword);

                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals(
                                response.getBody().getMessages().get(0),
                                oldPasswordDoesNotEqualToUserPassword.getMessage());
        }

        @Test
        void it_shouldReturn400WhenNewPasswordShouldNotEqualsToPrevousPassword() {
                NewPasswordShouldNotEqualsToPreviousPassword newPasswordShouldNotEqualsToPrevousPassword = new NewPasswordShouldNotEqualsToPreviousPassword();

                ResponseEntity<BadRequestOutput> response = changePasswordControlExceptionHandler
                                .handleNewPasswordShouldNotEqualsToPreviousPassword(
                                                newPasswordShouldNotEqualsToPrevousPassword);

                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals(
                                response.getBody().getMessages().get(0),
                                newPasswordShouldNotEqualsToPrevousPassword.getMessage());
        }

        @Test
        void it_shouldReturn500WhenCouldNotSaveUser() {
                CouldNotSaveUser couldNotSaveUser = new CouldNotSaveUser("some error has occurred");

                ResponseEntity<Void> response = changePasswordControlExceptionHandler
                                .handleCouldNotSaveUser(couldNotSaveUser);

                assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
}

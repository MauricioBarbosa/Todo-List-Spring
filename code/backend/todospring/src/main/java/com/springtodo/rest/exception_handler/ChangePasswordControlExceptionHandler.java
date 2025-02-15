package com.springtodo.rest.exception_handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
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

@ControllerAdvice
@Slf4j
public class ChangePasswordControlExceptionHandler {

    @ExceptionHandler({ MissingRequestHeaderException.class })
    public ResponseEntity<BadRequestOutput> handleMissingRequestHeader(
            MissingRequestHeaderException ex) {

        ArrayList<String> messages = new ArrayList<String>();

        messages.add("Required header '" + ex.getHeaderName() + "' is not present.");

        BadRequestOutput badRequestOutput = new BadRequestOutput();

        badRequestOutput.setMessages(messages);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequestOutput);
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<BadRequestOutput> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {

        final List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        ArrayList<String> messages = new ArrayList<String>();

        for (FieldError fieldError : fieldErrors) {
            messages.add(fieldError.getDefaultMessage());
        }

        BadRequestOutput badRequestOutput = new BadRequestOutput();

        badRequestOutput.setMessages(messages);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequestOutput);
    }

    @ExceptionHandler({ InvalidToken.class })
    public ResponseEntity<UnauthorizedOutput> handleInvalidToken(InvalidToken invalidToken) {
        UnauthorizedOutput unauthorizedOutput = new UnauthorizedOutput();

        ArrayList<String> messages = new ArrayList<String>();

        messages.add(invalidToken.getMessage());

        unauthorizedOutput.setMessages(messages);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                unauthorizedOutput);
    }

    @ExceptionHandler({ SessionNotFound.class })
    public ResponseEntity<UnauthorizedOutput> handleSessionNotFound(SessionNotFound sessionNotFound) {
        UnauthorizedOutput unauthorizedOutput = new UnauthorizedOutput();

        ArrayList<String> messages = new ArrayList<String>();

        messages.add(sessionNotFound.getMessage());

        unauthorizedOutput.setMessages(messages);

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
        ArrayList<String> errors = new ArrayList<String>();

        errors.add(invalidPassword.getMessage());

        badRequestOutput.setMessages(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                badRequestOutput);
    }

    @ExceptionHandler({ OldPasswordDoesNotEqualToUserPassword.class })
    public ResponseEntity<BadRequestOutput> handleOldPasswordDoesNotEqualToUserPassword(
            OldPasswordDoesNotEqualToUserPassword oldPasswordDoesNotEqualToUserPassword) {
        BadRequestOutput badRequestOutput = new BadRequestOutput();
        ArrayList<String> errors = new ArrayList<String>();

        errors.add(oldPasswordDoesNotEqualToUserPassword.getMessage());

        badRequestOutput.setMessages(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                badRequestOutput);
    }

    @ExceptionHandler({ NewPasswordShouldNotEqualsToPreviousPassword.class })
    public ResponseEntity<BadRequestOutput> handleNewPasswordShouldNotEqualsToPreviousPassword(
            NewPasswordShouldNotEqualsToPreviousPassword newPasswordShouldNotEqualsToPreviousPassword) {
        BadRequestOutput badRequestOutput = new BadRequestOutput();
        ArrayList<String> errors = new ArrayList<String>();

        errors.add(newPasswordShouldNotEqualsToPreviousPassword.getMessage());

        badRequestOutput.setMessages(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                badRequestOutput);
    }

    @ExceptionHandler({ CouldNotSaveUser.class })
    public ResponseEntity<Void> handleCouldNotSaveUser(
            CouldNotSaveUser couldNotSaveUser) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

package com.springtodo.rest.exception_handler;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.springtodo.core.identity_and_access.application.exception.CouldNotGenerateToken;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSaveSession;
import com.springtodo.core.identity_and_access.domain.exception.UserNotFoundException;
import com.springtodo.rest.pojo.shared.BadRequestOutput;

public class StartChangePasswordSessionExceptionHandler {
    @ExceptionHandler({ UserNotFoundException.class })
    public ResponseEntity<BadRequestOutput> handleUserNotFoundException(UserNotFoundException userNotFoundException) {
        BadRequestOutput badRequestOutput = new BadRequestOutput();
        ArrayList<String> errors = new ArrayList<String>();

        errors.add(userNotFoundException.getMessage());

        badRequestOutput.setMessages(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequestOutput);
    }

    @ExceptionHandler({ CouldNotRetrieveUser.class })
    public ResponseEntity<Void> handleCouldNotRetrieveUser(CouldNotRetrieveUser couldNotRetrieveUser) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler({ CouldNotSaveSession.class })
    public ResponseEntity<Void> handleCouldNotSaveSession(CouldNotSaveSession couldNotSaveSession) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler({ CouldNotGenerateToken.class })
    public ResponseEntity<Void> handleCouldNotGenerateToken(CouldNotGenerateToken couldNotGenerateToken) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

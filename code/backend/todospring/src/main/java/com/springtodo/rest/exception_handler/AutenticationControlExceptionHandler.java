package com.springtodo.rest.exception_handler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.springtodo.core.autentication.domain.exception.CouldNotCreateSession;
import com.springtodo.core.autentication.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.autentication.domain.exception.InvalidPassword;
import com.springtodo.core.autentication.domain.exception.UserNotFoundException;

@ControllerAdvice
public class AutenticationControlExceptionHandler {
    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, Object> responsePayload = new HashMap<>();
        responsePayload.put("messages", Arrays.copyOfRange(exception.getDetailMessageArguments(), 1,
                exception.getDetailMessageArguments().length));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST_400)
                .body(responsePayload);
    }

    @ExceptionHandler({ CouldNotCreateSession.class })
    public ResponseEntity<Object> handleCouldNotCreateSessionException(
            CouldNotCreateSession couldNotCreateSessionException) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR_500).body(null);
    }

    @ExceptionHandler({ CouldNotRetrieveUser.class })
    public ResponseEntity<Object> handleWithCouldNotRetrieveUser(CouldNotRetrieveUser couldNotRetrieveUserException) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR_500).body(null);
    }

    @ExceptionHandler({ UserNotFoundException.class })
    public ResponseEntity<Object> handleWithUserNotFound(UserNotFoundException userNotFoundException) {
        Map<String, Object> responsePayload = new HashMap<>();
        responsePayload.put("message", userNotFoundException.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST_400).body(responsePayload);
    }

    @ExceptionHandler({ InvalidPassword.class })
    public ResponseEntity<Object> handleWithInvalidPassword(InvalidPassword invalidPassword) {
        Map<String, Object> responsePayload = new HashMap<>();
        responsePayload.put("message", invalidPassword.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST_400).body(responsePayload);
    }
}

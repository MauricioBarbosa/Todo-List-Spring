package com.springtodo.rest.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springtodo.core.identity_and_access.application.usecase.Authenticate;
import com.springtodo.rest.pojo.authenticate.AuthenticateRequestBody;
import com.springtodo.rest.pojo.authenticate.AuthenticateResponse;

import jakarta.validation.Valid;

@RestController
public class AuthenticateControl {
    @Autowired
    private Authenticate authenticate;

    public ResponseEntity<AuthenticateResponse> authenticate(
            @Valid @RequestBody AuthenticateRequestBody authenticateRequest) {

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}

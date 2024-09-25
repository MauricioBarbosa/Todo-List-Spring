package com.springtodo.rest.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springtodo.core.autentication.application.usecase.authentication.AuthenticateUseCase;
import com.springtodo.core.autentication.application.usecase.authentication.dto.InputDTO;
import com.springtodo.core.autentication.application.usecase.authentication.dto.OutputDTO;
import com.springtodo.core.autentication.domain.exception.CouldNotCreateSession;
import com.springtodo.core.autentication.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.autentication.domain.exception.InvalidPassword;
import com.springtodo.core.autentication.domain.exception.UserNotFoundException;
import com.springtodo.rest.pojo.AuthenticationInputJson;

import jakarta.validation.Valid;

@RestController
public class AutenticationControl {

    @Autowired
    private AuthenticateUseCase authenticationUseCase;

    @PostMapping("/login")
    public ResponseEntity<OutputDTO> login(@Valid @RequestBody AuthenticationInputJson authenticationInputJson)
            throws UserNotFoundException, CouldNotCreateSession, CouldNotRetrieveUser, InvalidPassword {

        OutputDTO result = authenticationUseCase
                .execute(new InputDTO(authenticationInputJson.getEmail(), authenticationInputJson.getPassword()));

        return ResponseEntity.status(HttpStatus.OK).body(result);

    }
}

package com.springtodo.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.springtodo.core.autentication.application.usecase.authenticate.AuthenticateUseCase;
import com.springtodo.core.autentication.application.usecase.authenticate.dto.InputDTO;
import com.springtodo.core.autentication.application.usecase.authenticate.dto.OutputDTO;
import com.springtodo.core.autentication.domain.exception.CouldNotCreateSession;
import com.springtodo.core.autentication.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.autentication.domain.exception.InvalidPassword;
import com.springtodo.core.autentication.domain.exception.UserNotFoundException;
import com.springtodo.rest.model.AuthenticationInputJson;

@RestController
public class AutenticationControl {

    @Autowired
    private AuthenticateUseCase authenticationUseCase;

    @PostMapping("/login")
    public String login(@RequestBody AuthenticationInputJson authenticationInputJson) {
        try {
            OutputDTO result = authenticationUseCase
                    .execute(new InputDTO(authenticationInputJson.getEmail(), authenticationInputJson.getPassword()));

            return result.getToken();
        } catch (CouldNotCreateSession | CouldNotRetrieveUser e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        } catch (UserNotFoundException | InvalidPassword e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }
}

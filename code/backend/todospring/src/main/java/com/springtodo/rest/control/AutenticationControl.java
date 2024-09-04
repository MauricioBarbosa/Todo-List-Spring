package com.springtodo.rest.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springtodo.core.autentication.application.usecase.authenticate.AuthenticateUseCase;
import com.springtodo.core.autentication.application.usecase.authenticate.dto.InputDTO;
import com.springtodo.core.autentication.application.usecase.authenticate.dto.OutputDTO;
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
    public String login(@Valid @RequestBody AuthenticationInputJson authenticationInputJson)
            throws UserNotFoundException, CouldNotCreateSession, CouldNotRetrieveUser, InvalidPassword {

        OutputDTO result = authenticationUseCase
                .execute(new InputDTO(authenticationInputJson.getEmail(), authenticationInputJson.getPassword()));

        return result.getToken();

    }
}

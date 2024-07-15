package com.springtodo.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springtodo.core.autentication.application.usecase.authenticate.AuthenticateUseCase;
import com.springtodo.core.autentication.application.usecase.authenticate.dto.InputDTO;
import com.springtodo.rest.model.AuthenticationInputJson;

@RestController
public class Autentication {

    private AuthenticateUseCase authenticationUseCase;

    @Autowired
    public Autentication(AuthenticateUseCase authenticationUseCase) {
        this.authenticationUseCase = authenticationUseCase;
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthenticationInputJson authenticationInputJson) {
        authenticationUseCase.execute(new InputDTO("Mauricio", "password"));
        return "My application is running";
    }
}

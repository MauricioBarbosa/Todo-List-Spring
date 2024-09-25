package com.springtodo.core.autentication.application.usecase.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springtodo.core.autentication.application.usecase.authentication.dto.InputDTO;
import com.springtodo.core.autentication.application.usecase.authentication.dto.OutputDTO;
import com.springtodo.core.autentication.domain.exception.CouldNotCreateSession;
import com.springtodo.core.autentication.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.autentication.domain.exception.InvalidPassword;
import com.springtodo.core.autentication.domain.exception.UserNotFoundException;
import com.springtodo.core.autentication.domain.service.AuthorizationService;

@Service
public class AuthenticateUseCase {

    @Autowired
    private AuthorizationService authorizationService;

    public OutputDTO execute(InputDTO inputDto)
            throws UserNotFoundException, CouldNotCreateSession, CouldNotRetrieveUser, InvalidPassword {

        String sessionToken = authorizationService.startSession(inputDto.getEmail(), inputDto.getPassword());

        return new OutputDTO(sessionToken);
    }
}
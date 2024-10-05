package com.springtodo.core.identity_and_access.domain.service;

import com.springtodo.core.identity_and_access.domain.entity.Session;
import com.springtodo.core.identity_and_access.domain.value_object.ConfirmationCode;
import com.springtodo.core.identity_and_access.domain.value_object.SessionId;
import com.springtodo.core.identity_and_access.domain.value_object.UserEmail;
import com.springtodo.core.identity_and_access.domain.value_object.UserPassword;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    public SessionService() {}

    public Session startSession(
        UserPassword aUserPassword,
        UserEmail aUserEmail
    ) {
        /**
        Para começar uma sessão, devemos recuperar o usuário de algum local registrado e verificar se ele existe, e caso ele exista, verificamos seu password. Se o
        password estiver correto, inicie a sessão com uma duração de 30 minutos
        */
        String string = "Not implemented yet";
        throw new RuntimeException(string);
    }

    public void confirmSession(
        SessionId aSessionId,
        ConfirmationCode aConfirmationCode
    ) {
        String string = "Not implemented yet";
        throw new RuntimeException(string);
    }
}

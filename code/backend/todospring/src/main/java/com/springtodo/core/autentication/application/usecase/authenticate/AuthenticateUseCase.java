package com.springtodo.core.autentication.application.usecase.authenticate;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springtodo.core.autentication.application.usecase.authenticate.dto.InputDTO;
import com.springtodo.core.autentication.application.usecase.authenticate.dto.OutputDTO;
import com.springtodo.core.autentication.domain.service.AuthorizationService;

@Service
public class AuthenticateUseCase {

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private Logger LOG;

    public OutputDTO execute(InputDTO inputDto) {

        /**
         * 1 - Recupera o usuário do banco de dados
         * 1.1 - Erro ao recuperar usuário do banco de dados
         * 1.1.1 - Lança Exceção
         * 2 - Confirme que o usuário é autentico
         * 2.2 - O usuário não é autentico
         * 2.2.2 - Lança Exceção
         * 3 - Gera o token de sessão
         * 3.1 - Erro ao gerar o token de sessão
         * 3.1.1 - Lança Exceção
         */

        throw new RuntimeException("Not implemented yet");
    }
}
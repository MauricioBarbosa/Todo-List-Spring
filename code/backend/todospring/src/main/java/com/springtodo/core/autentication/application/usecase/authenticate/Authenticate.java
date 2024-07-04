package com.springtodo.core.autentication.application.usecase.authenticate;

import com.springtodo.core.autentication.application.usecase.authenticate.dto.InputDTO;
import com.springtodo.core.autentication.application.usecase.authenticate.dto.OutputDTO;

public class Authenticate {
    Authenticate() {
    }

    public OutputDTO execute(InputDTO inputDto) {
        /**
         * 1 - Recuperar um usuário do banco de dados
         * 2 - Autenticá-lo(verificar se a senha bate com a informada e o email também)
         * 3 - Gerar um token
         */
        return new OutputDTO();
    }
}
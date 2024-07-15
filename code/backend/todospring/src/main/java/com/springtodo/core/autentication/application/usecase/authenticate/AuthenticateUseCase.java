package com.springtodo.core.autentication.application.usecase.authenticate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springtodo.config.PersistenceConfig;
import com.springtodo.core.autentication.application.usecase.authenticate.dto.InputDTO;
import com.springtodo.core.autentication.application.usecase.authenticate.dto.OutputDTO;
import com.springtodo.core.autentication.domain.repository.UserRepository;

@Service
public class AuthenticateUseCase {

    @Autowired
    private UserRepository userRepository;

    private static final Logger LOG = LoggerFactory.getLogger(PersistenceConfig.class);

    public OutputDTO execute(InputDTO inputDto) {
        try {
            userRepository.getUser(inputDto.getEmail());
        } catch (Exception e) {
            LOG.error("error on getting user", e);
        }

        /**
         * 1 - Recuperar um usuário do banco de dados
         * 2 - Autenticá-lo(verificar se a senha bate com a informada e o email também)
         * 3 - Gerar um token
         */
        return new OutputDTO();
    }
}
package com.springtodo.core.autentication.domain.service;

import com.springtodo.core.autentication.domain.enums.ConfirmationCodeChannels;

public class ConfirmationService {
    private TokenGeneratorService tokenGeneratorService;

    ConfirmationService() {
    }

    public void sendConfirmationCode(ConfirmationCodeChannels channel) {
        /**
         * 1 - Gerar o codigo de confirmação
         */
    }
}

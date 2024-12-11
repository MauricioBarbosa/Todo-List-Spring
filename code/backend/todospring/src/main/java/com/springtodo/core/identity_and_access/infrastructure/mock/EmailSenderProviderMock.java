package com.springtodo.core.identity_and_access.infrastructure.mock;

import com.springtodo.core.identity_and_access.domain.entity.User;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSendEmail;
import com.springtodo.core.identity_and_access.domain.provider.EmailSenderProvider;
import com.springtodo.core.identity_and_access.domain.value_object.ConfirmationCode;

public class EmailSenderProviderMock extends EmailSenderProvider {
    @Override
    public void sendConfirmationCode(ConfirmationCode confirmationCode, User user) throws CouldNotSendEmail {
        System.out.println("Sending email mock...");

        System.out.println("Email sent..");

    }
}

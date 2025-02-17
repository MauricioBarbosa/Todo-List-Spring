package com.springtodo.core.identity_and_access.domain.provider;

import com.springtodo.core.identity_and_access.domain.entity.User;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSendEmail;
import com.springtodo.core.identity_and_access.domain.value_object.ConfirmationCode;

public abstract class EmailProvider {
    public abstract void sendConfirmationCode(ConfirmationCode confirmationCode, User user) throws CouldNotSendEmail;
}

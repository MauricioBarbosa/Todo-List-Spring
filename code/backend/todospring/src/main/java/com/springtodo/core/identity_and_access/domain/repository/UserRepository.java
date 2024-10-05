package com.springtodo.core.identity_and_access.domain.repository;

import com.springtodo.core.identity_and_access.domain.entity.User;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.identity_and_access.domain.exception.UserNotFoundException;
import com.springtodo.core.identity_and_access.domain.value_object.UserEmail;

public abstract class UserRepository {

    protected UserRepository() {}

    public abstract User getUserByEmail(UserEmail anUserEmail)
        throws UserNotFoundException, CouldNotRetrieveUser;
}

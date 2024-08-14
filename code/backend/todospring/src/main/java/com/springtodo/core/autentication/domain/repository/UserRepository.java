package com.springtodo.core.autentication.domain.repository;

import com.springtodo.core.autentication.domain.entity.User;
import com.springtodo.core.autentication.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.autentication.domain.exception.UserNotFoundException;

public abstract class UserRepository {
    protected UserRepository() {
    }

    public abstract User getUserByEmailAddress(String email)
            throws UserNotFoundException, CouldNotRetrieveUser;
}

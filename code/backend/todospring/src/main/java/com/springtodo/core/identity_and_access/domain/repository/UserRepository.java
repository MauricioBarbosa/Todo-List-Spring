package com.springtodo.core.identity_and_access.domain.repository;

import org.springframework.cache.annotation.Cacheable;

import com.springtodo.core.identity_and_access.domain.entity.User;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.identity_and_access.domain.exception.UserNotFoundException;
import com.springtodo.core.identity_and_access.domain.value_object.UserEmail;
import com.springtodo.core.identity_and_access.domain.value_object.UserId;

public abstract class UserRepository {

    @Cacheable("user")
    public abstract User getUserByEmail(UserEmail anUserEmail)
            throws UserNotFoundException, CouldNotRetrieveUser;

    @Cacheable("user")
    public abstract User getUserById(UserId userId)
            throws UserNotFoundException, CouldNotRetrieveUser;
}

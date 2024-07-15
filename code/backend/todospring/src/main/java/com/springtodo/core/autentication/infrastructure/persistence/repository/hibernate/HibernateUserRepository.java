package com.springtodo.core.autentication.infrastructure.persistence.repository.hibernate;

import com.springtodo.core.autentication.domain.entity.User;
import com.springtodo.core.autentication.domain.exception.UserNotFoundException;
import com.springtodo.core.autentication.domain.repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class HibernateUserRepository extends UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User getUser(String email) throws UserNotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUser'");
        /**
         * 1 - Find user by email
         * 1.1 - An error has occured on finding user
         * 1.1.1 - Log error has occured on finding user
         * 1.1.2 - Throw this error
         * 1.2 - User not found
         * 1.2.1 - Log user not found
         * 1.2.2 - Throw user not found exception
         * 2 - Return a new user
         
         */
    }

}

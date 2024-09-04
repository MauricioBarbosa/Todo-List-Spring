package com.springtodo.core.autentication.infrastructure.persistence.repository.hibernate_impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.springtodo.core.autentication.domain.entity.User;
import com.springtodo.core.autentication.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.autentication.domain.exception.UserNotFoundException;
import com.springtodo.core.autentication.domain.repository.UserRepository;
import com.springtodo.core.autentication.infrastructure.persistence.repository.hibernate_impl.jpa.model.UserJpa;
import com.springtodo.core.autentication.infrastructure.persistence.repository.hibernate_impl.jpa.repository.UserJpaRepository;

import jakarta.persistence.NoResultException;

@Component
public class HibernateUserRepository extends UserRepository {

    private Logger log = LoggerFactory.getLogger(HibernateUserRepository.class);

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Override
    public User getUserByEmailAddress(String email) throws UserNotFoundException, CouldNotRetrieveUser {
        try {
            UserJpa userJpa = userJpaRepository.getByEmailAddress(email);

            if (userJpa == null) {
                throw new NoResultException("User not found");
            }

            return new User(Long.toString(userJpa.getId()), userJpa.getEmail(), userJpa.getPassword());
        } catch (NoResultException e) {
            log.error("User not found with email: " + email, e);
            throw new UserNotFoundException("User not found with email: " + email);
        } catch (Exception e) {
            log.error("An error has occurred on recovering user", e);
            throw new CouldNotRetrieveUser(e.getMessage());
        }
    }

}

package com.springtodo.core.identity_and_access.infrastructure.persistence.repository.hibernate;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.springtodo.core.identity_and_access.domain.entity.User;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSaveUser;
import com.springtodo.core.identity_and_access.domain.exception.UserNotFoundException;
import com.springtodo.core.identity_and_access.domain.repository.UserRepository;
import com.springtodo.core.identity_and_access.domain.value_object.UserEmail;
import com.springtodo.core.identity_and_access.domain.value_object.UserId;
import com.springtodo.core.identity_and_access.domain.value_object.UserPassword;
import com.springtodo.core.identity_and_access.infrastructure.persistence.jpa.entity.UserJpa;
import com.springtodo.core.identity_and_access.infrastructure.persistence.jpa.repository.UserJpaRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HibernateUserRepository extends UserRepository {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Override
    public User getUserByEmail(UserEmail anUserEmail)
            throws UserNotFoundException, CouldNotRetrieveUser {
        try {
            UserJpa userJpa = userJpaRepository.getByEmailAddress(anUserEmail.getEmail());

            if (userJpa == null) {
                throw new UserNotFoundException("user of email" + anUserEmail.getEmail() + " not found");
            }

            User user = new User(
                    new UserId(userJpa.getId().toString()),
                    new UserEmail(userJpa.getEmail()),
                    new UserPassword(userJpa.getPassword()));

            return user;
        } catch (UserNotFoundException e) {
            log.error("user not found", e);

            throw e;
        } catch (Exception e) {
            log.error("could not retrieve user by email", e);

            throw new CouldNotRetrieveUser(e.getMessage());
        }
    }

    @Override
    public User getUserById(UserId userId)
            throws UserNotFoundException, CouldNotRetrieveUser {
        try {
            UserJpa userJpa = userJpaRepository.getById(UUID.fromString(userId.getId()));

            if (userJpa == null) {
                throw new UserNotFoundException("user of id: " + userId.getId() + " not found");
            }

            User user = new User(
                    new UserId(userJpa.getId().toString()),
                    new UserEmail(userJpa.getEmail()),
                    new UserPassword(userJpa.getPassword()));

            return user;
        } catch (UserNotFoundException e) {
            log.error("user not found, userId: {}", userId, e);

            throw e;
        } catch (Exception e) {
            log.error("could not retrieve user by id", e);

            throw new CouldNotRetrieveUser(e.getMessage());
        }
    }

    @Override
    public void save(User user) throws CouldNotSaveUser {
        try {
            log.info("translating user to user jpa, user: {}", user);

            UserJpa userJpa = new UserJpa();

            userJpa.setId(UUID.fromString(user.getId().getId()));
            userJpa.setEmail(user.getEmail().getEmail());
            userJpa.setName("Some User");
            userJpa.setFullname("some fullname");
            userJpa.setPassword(user.getPassword().getPassword());
            userJpa.setPicture_path("");

            log.info("user translated, saving user, user: {}, userJpa: {}", user, userJpa);

            this.userJpaRepository.save(userJpa);

        } catch (Exception e) {
            log.error("could not save user , user: {}, error: {}", user, e);

            throw new CouldNotSaveUser(e.getMessage());
        }
    }
}

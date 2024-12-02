package com.springtodo.core.identity_and_access.infrastructure.persistence.repository.hibernate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.springtodo.core.identity_and_access.domain.entity.User;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.identity_and_access.domain.exception.UserNotFoundException;
import com.springtodo.core.identity_and_access.domain.repository.UserRepository;
import com.springtodo.core.identity_and_access.domain.value_object.UserEmail;
import com.springtodo.core.identity_and_access.domain.value_object.UserId;
import com.springtodo.core.identity_and_access.domain.value_object.UserPassword;
import com.springtodo.core.identity_and_access.infrastructure.persistence.jpa.entity.UserJpa;
import com.springtodo.core.identity_and_access.infrastructure.persistence.jpa.repository.UserJpaRepository;

@Component
public class HibernateUserRepository extends UserRepository {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Override
    public User getUserByEmail(UserEmail anUserEmail)
        throws UserNotFoundException, CouldNotRetrieveUser {
        try {
            UserJpa userJpa = userJpaRepository.getByEmailAddress(anUserEmail.getEmail());

            if(userJpa == null){
                throw new UserNotFoundException("user of email" + anUserEmail.getEmail() + " not found");
            }

            User user = new User(
                new UserId(userJpa.getId().toString()),
                new UserEmail(userJpa.getEmail()),
                new UserPassword(userJpa.getPassword())
            );

            return user;
        } catch (UserNotFoundException e) {
            throw e;
        } catch(Exception e){
            throw new CouldNotRetrieveUser(e.getMessage()); 
        }
    }

    @Override
    public User getUserById(UserId userId)
        throws UserNotFoundException, CouldNotRetrieveUser {
            try {
                UserJpa userJpa = userJpaRepository.getById(userId.getId());

                if(userJpa == null){
                    throw new UserNotFoundException("user of id: " + userId.getId() + " not found");
                }

                User user = new User(
                    new UserId(userJpa.getId().toString()),
                    new UserEmail(userJpa.getEmail()),
                    new UserPassword(userJpa.getPassword())
                );

                return user;
            } 
            catch (UserNotFoundException e) {
                throw e;
            }
            catch(Exception e){
                throw new CouldNotRetrieveUser(e.getMessage()); 
            }
    }
}

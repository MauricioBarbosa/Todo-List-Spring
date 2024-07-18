package com.springtodo.core.autentication.infrastructure.persistence.repository.hibernate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.springtodo.core.autentication.domain.entity.User;
import com.springtodo.core.autentication.domain.exception.UserNotFoundException;
import com.springtodo.core.autentication.domain.repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

public class HibernateUserRepository extends UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger LOG = LoggerFactory.getLogger(HibernateUserRepository.class);

    @Override
    public User getUser(String email) throws UserNotFoundException {
        try {
            TypedQuery<User> query = entityManager.createQuery(
                    "SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            LOG.error("User not found with email: " + email, e);
            throw new UserNotFoundException("User not found with email: " + email);
        } catch (Exception e) {
            LOG.error("An error has occurred on recovering user");
            throw new RuntimeException("An error has occurred");
        }
    }

}

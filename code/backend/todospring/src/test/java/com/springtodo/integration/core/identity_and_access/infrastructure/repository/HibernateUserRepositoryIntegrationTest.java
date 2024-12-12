package com.springtodo.integration.core.identity_and_access.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.springtodo.core.identity_and_access.domain.entity.User;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.identity_and_access.domain.exception.UserNotFoundException;
import com.springtodo.core.identity_and_access.domain.value_object.UserEmail;
import com.springtodo.core.identity_and_access.infrastructure.persistence.jpa.entity.UserJpa;
import com.springtodo.core.identity_and_access.infrastructure.persistence.repository.hibernate.HibernateUserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class HibernateUserRepositoryIntegrationTest {

    @Autowired
    private HibernateUserRepository hibernateUserRepository;

    @Autowired
    private EntityManager entityManager;

    private String userName = "Some user";
    private String userEmail = "some@email1.com";
    private String userPassword = "#somePassword";
    private String userFullname = "Some full name";

    @BeforeEach
    void setUp() {
        UserJpa userJpa = new UserJpa();

        userJpa.setName(this.userName);
        userJpa.setEmail(this.userEmail);
        userJpa.setPassword(this.userPassword);
        userJpa.setFullname(this.userFullname);

        entityManager.persist(userJpa);

        entityManager.flush();
    }

    @Nested
    class TestGetUserByEmail {

        @Test
        void should_ThrowUserNotFoundException() {
            UserEmail fakeUserEmail = new UserEmail(
                "somefakeuseremail@email.com"
            );

            assertThrows(UserNotFoundException.class, () -> {
                hibernateUserRepository.getUserByEmail(fakeUserEmail);
            });
        }

        @Test
        void should_ReturnAUserEntity()
            throws UserNotFoundException, CouldNotRetrieveUser {
            UserEmail userEmailValueObject = new UserEmail(userEmail);

            User user = hibernateUserRepository.getUserByEmail(
                userEmailValueObject
            );

            assertEquals(user.getEmail(), userEmailValueObject);
            assertEquals(user.getPassword().getPassword(), userPassword);
        }
    }
}

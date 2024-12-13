package com.springtodo.unit.core.identity_and_access.infrastructure.repository.hibernate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.springtodo.core.identity_and_access.domain.entity.User;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.identity_and_access.domain.exception.UserNotFoundException;
import com.springtodo.core.identity_and_access.domain.value_object.UserEmail;
import com.springtodo.core.identity_and_access.domain.value_object.UserId;
import com.springtodo.core.identity_and_access.domain.value_object.UserPassword;
import com.springtodo.core.identity_and_access.infrastructure.persistence.jpa.entity.UserJpa;
import com.springtodo.core.identity_and_access.infrastructure.persistence.jpa.repository.UserJpaRepository;
import com.springtodo.core.identity_and_access.infrastructure.persistence.repository.hibernate.HibernateUserRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class HibernateUserRepositoryUnitTest {

    @Mock
    private UserJpaRepository userJpaRepository;

    @InjectMocks
    private HibernateUserRepository hibernateUserRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Testing SessionService.sendConfirmationCode")
    class TestGetUserByEmail {

        private UserEmail userEmail = new UserEmail("someUserEmail@email.com");

        @Test
        void should_ThrowCouldNotRetrieveUser() {
            RuntimeException exception = new RuntimeException("Some exception has thrown");

            when(
                userJpaRepository.getByEmailAddress(userEmail.getEmail())
            ).thenThrow(exception);

            assertThrows(CouldNotRetrieveUser.class, () -> {
                hibernateUserRepository.getUserByEmail(userEmail);
            });
        }

        @Test
        void should_ThrowUserNotFoundException() {
            when(
                userJpaRepository.getByEmailAddress(userEmail.getEmail())
            ).thenReturn(null);

            assertThrows(UserNotFoundException.class, () -> {
                hibernateUserRepository.getUserByEmail(userEmail);
            });
        }

        @Test
        void should_ReturnAnUser() {
            UserJpa userJpa = new UserJpa();

            userJpa.setName("Some user name");
            userJpa.setEmail("someuseremail@email.com");
            userJpa.setPassword("somePassword");
            userJpa.setId(UUID.randomUUID());

            User user = new User(
                new UserId(userJpa.getId().toString()),
                new UserEmail(userJpa.getEmail()),
                new UserPassword(userJpa.getPassword())
            );

            when(
                userJpaRepository.getByEmailAddress(userEmail.getEmail())
            ).thenReturn(userJpa);

            assertEquals(userJpa.getId().toString(), user.getId().getId());
        }
    }

    @Nested
    @DisplayName("Testing SessionService.sendConfirmationCode")
    class TestGetUserById {

        private UserId userId = new UserId("someUserId");

        @Test
        void should_ThrowCouldNotRetrieveUser() {
            RuntimeException exception = new RuntimeException("Some exception has thrown");

            when(userJpaRepository.getById(userId.getId())).thenThrow(
                exception
            );

            assertThrows(CouldNotRetrieveUser.class, () -> {
                hibernateUserRepository.getUserById(userId);
            });
        }

        void should_ThrowUserNotFoundException() {
            when(
                userJpaRepository.getById(userId.getId())
            ).thenReturn(null);

            assertThrows(UserNotFoundException.class, () -> {
                hibernateUserRepository.getUserById(userId);
            });
        }

        void should_ReturnAnUser() {
            UserJpa userJpa = new UserJpa();

            userJpa.setName("Some user name");
            userJpa.setEmail("someuseremail@email.com");
            userJpa.setPassword("somePassword");
            userJpa.setId(UUID.randomUUID());

            User user = new User(
                new UserId(userJpa.getId().toString()),
                new UserEmail(userJpa.getEmail()),
                new UserPassword(userJpa.getPassword())
            );

            when(
                userJpaRepository.getById(userId.getId())
            ).thenReturn(userJpa);

            assertEquals(userJpa.getId().toString(), user.getId().getId());
        }
    }
}

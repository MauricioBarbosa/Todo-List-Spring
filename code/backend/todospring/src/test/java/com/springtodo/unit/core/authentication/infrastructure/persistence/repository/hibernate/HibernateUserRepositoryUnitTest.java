package com.springtodo.unit.core.authentication.infrastructure.persistence.repository.hibernate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.springtodo.core.autentication.domain.entity.User;
import com.springtodo.core.autentication.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.autentication.domain.exception.UserNotFoundException;
import com.springtodo.core.autentication.infrastructure.persistence.repository.hibernate_impl.HibernateUserRepository;
import com.springtodo.core.autentication.infrastructure.persistence.repository.hibernate_impl.jpa.model.UserJpa;
import com.springtodo.core.autentication.infrastructure.persistence.repository.hibernate_impl.jpa.repository.UserJpaRepository;

import jakarta.persistence.EntityManager;

@ExtendWith(MockitoExtension.class)
public class HibernateUserRepositoryUnitTest {

    @InjectMocks
    HibernateUserRepository hibernateUserRepository;

    @Mock
    UserJpaRepository userJpaRepository;

    @Mock
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("It must throw an error when entityManager throws error")
    void testWhenEntityManagerThrowsError() {
        RuntimeException error = new RuntimeException("error has occurred");
        String email = "someemail@dot.com";

        when(userJpaRepository.getByEmailAddress(email)).thenThrow(error);

        assertThrows(CouldNotRetrieveUser.class, () -> this.hibernateUserRepository.getUserByEmailAddress(email));
    }

    @Test
    @DisplayName("It must throw user not found exception when user is not found")
    void testWhenUserIsNotFound() {
        String email = "someemail@dot.com";

        when(userJpaRepository.getByEmailAddress(email)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> this.hibernateUserRepository.getUserByEmailAddress(email));
    }

    @Test
    @DisplayName("It must return an user")
    void testWhenEntityManagerReturnsUser() throws UserNotFoundException, CouldNotRetrieveUser {
        String email = "someemail@dot.com";
        String password = "somepassword";
        String id = Long.toString(0L);

        User user = new User(id, email, password);
        UserJpa userJpa = new UserJpa();
        userJpa.setEmail(email);
        userJpa.setId(0L);
        userJpa.setPassword(password);

        when(userJpaRepository.getByEmailAddress(email)).thenReturn(userJpa);

        User foundUser = this.hibernateUserRepository.getUserByEmailAddress(email);

        assertEquals(foundUser.getId(), user.getId());
    }
}

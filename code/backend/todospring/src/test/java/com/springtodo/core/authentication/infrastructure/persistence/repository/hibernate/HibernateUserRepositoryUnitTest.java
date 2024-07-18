package com.springtodo.core.authentication.infrastructure.persistence.repository.hibernate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
import com.springtodo.core.autentication.domain.exception.UserNotFoundException;
import com.springtodo.core.autentication.infrastructure.persistence.repository.hibernate.HibernateUserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@ExtendWith(MockitoExtension.class)
public class HibernateUserRepositoryUnitTest {

    @InjectMocks
    HibernateUserRepository hibernateUserRepository;

    @Mock
    private TypedQuery<User> query;

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

        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getSingleResult()).thenThrow(error);

        assertThrows(error.getClass(), () -> this.hibernateUserRepository.getUser(email));
    }

    @Test
    @DisplayName("It must throw user not found exception when user is not found")
    void testWhenUserIsNotFound() {
        NoResultException noResultException = new NoResultException();
        String email = "someemail@dot.com";

        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getSingleResult()).thenThrow(noResultException);

        assertThrows(UserNotFoundException.class, () -> this.hibernateUserRepository.getUser(email));
    }

    @Test
    @DisplayName("It must return an user")
    void testWhenEntityManagerReturnsUser() {
        String email = "someemail@dot.com";
        String password = "somepassword";
        String id = "someId";

        User user = new User(id, email, password);

        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(user);

        User foundUser = this.hibernateUserRepository.getUser(email);

        assertEquals(foundUser, user);
    }
}

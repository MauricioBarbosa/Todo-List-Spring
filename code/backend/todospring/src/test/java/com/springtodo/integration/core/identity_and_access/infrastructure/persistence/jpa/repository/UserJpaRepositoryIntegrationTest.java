package com.springtodo.integration.core.identity_and_access.infrastructure.persistence.jpa.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import com.springtodo.core.identity_and_access.infrastructure.persistence.jpa.entity.UserJpa;
import com.springtodo.core.identity_and_access.infrastructure.persistence.jpa.repository.UserJpaRepository;

@Transactional
@DataJpaTest
public class UserJpaRepositoryIntegrationTest {

    @Autowired
    private UserJpaRepository userJpaRepository;

    private String userName = "Some user";
    private String userEmail = "some@email1.com";
    private String userPassword = "#somePassword";
    private String userFullname = "Some full name";
    private UUID id = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        UserJpa userJpa = new UserJpa();

        userJpa.setName(this.userName);
        userJpa.setEmail(this.userEmail);
        userJpa.setPassword(this.userPassword);
        userJpa.setFullname(this.userFullname);
        userJpa.setId(id);

        userJpaRepository.save(userJpa);
    }

    @Nested
    class GetByEmailAddressIntegrationTest {

        @Test
        void should_ReturnAUser() {
            UserJpa result = userJpaRepository.getByEmailAddress(userEmail);

            assertNotNull(result);

            assertEquals(result.getEmail(), userEmail);
        }

        @Test
        void should_NotReturnAUser() {
            UserJpa result = userJpaRepository.getByEmailAddress(userEmail);

            assertNull(result);
        }
    }

    @Nested
    class GetByIdIntegrationTest {

        @Test
        void should_ReturnAUser() {
            UserJpa result = userJpaRepository.getById(id);

            assertNotNull(result);

            assertEquals(result.getId().toString(), id);
        }

        void should_NotReturnAUser() {
            UUID fakeId = UUID.randomUUID();

            UserJpa result = userJpaRepository.getById(fakeId);

            assertNull(result);
        }
    }
}

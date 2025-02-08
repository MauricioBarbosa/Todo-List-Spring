package com.springtodo.core.identity_and_access.infrastructure.persistence.jpa.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.springtodo.core.identity_and_access.infrastructure.persistence.jpa.entity.UserJpa;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpa, Long> {
    @Query("select u from UserJpa u where u.email = :emailAddress")
    UserJpa getByEmailAddress(@Param("emailAddress") String emailAddress);

    @Query("select u from UserJpa u where u.id = :id")
    UserJpa getById(@Param("id") UUID id);
}

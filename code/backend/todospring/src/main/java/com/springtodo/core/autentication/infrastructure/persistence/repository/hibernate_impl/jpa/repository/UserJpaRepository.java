package com.springtodo.core.autentication.infrastructure.persistence.repository.hibernate_impl.jpa.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.springtodo.core.autentication.infrastructure.persistence.repository.hibernate_impl.jpa.model.UserJpa;

import jakarta.persistence.NoResultException;

public interface UserJpaRepository extends CrudRepository<UserJpa, Long> {
    @Query("SELECT u FROM UserJpa u WHERE u.email = :email")
    UserJpa getByEmailAddress(@Param("email") String email) throws NoResultException;
}
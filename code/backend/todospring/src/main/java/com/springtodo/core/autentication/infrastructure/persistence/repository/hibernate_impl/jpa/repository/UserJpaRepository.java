package com.springtodo.core.autentication.infrastructure.persistence.repository.hibernate_impl.jpa.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.springtodo.core.autentication.infrastructure.persistence.repository.hibernate_impl.jpa.model.UserJpa;

public interface UserJpaRepository extends CrudRepository<UserJpa, Long> {
    @Query("select u from Users u where u.emailAddress = ?1")
    UserJpa getByEmailAddress(String emailAddress);
}
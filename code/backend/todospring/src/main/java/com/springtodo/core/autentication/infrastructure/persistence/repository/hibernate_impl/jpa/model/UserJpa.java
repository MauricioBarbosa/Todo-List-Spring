package com.springtodo.core.autentication.infrastructure.persistence.repository.hibernate_impl.jpa.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Users")
@Getter
@Setter
public class UserJpa {
    @Id
    @GeneratedValue
    private UUID id;

    private String email;

    private String name;

    private String password;

    private String fullname;

    public UserJpa() {
    }

}

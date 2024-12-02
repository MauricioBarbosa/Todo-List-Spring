package com.springtodo.core.identity_and_access.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
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
}

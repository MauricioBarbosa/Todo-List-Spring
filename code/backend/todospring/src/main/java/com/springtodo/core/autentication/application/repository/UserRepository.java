package com.springtodo.core.autentication.application.repository;

import com.springtodo.core.autentication.domain.entity.User;

public abstract class UserRepository {
    UserRepository() {
    }

    public abstract User getUser(String email);
}

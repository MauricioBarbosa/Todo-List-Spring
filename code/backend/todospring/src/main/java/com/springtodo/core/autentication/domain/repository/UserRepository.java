package com.springtodo.core.autentication.domain.repository;

import com.springtodo.core.autentication.domain.entity.User;

public abstract class UserRepository {
    protected UserRepository() {
    }

    public abstract User getUser(String email);
}

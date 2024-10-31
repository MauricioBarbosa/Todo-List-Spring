package com.springtodo.unit.core.identity_and_access.domain.builder;

import com.springtodo.core.identity_and_access.domain.entity.User;
import com.springtodo.core.identity_and_access.domain.exception.EmailNotInformed;
import com.springtodo.core.identity_and_access.domain.exception.IdNotInformed;
import com.springtodo.core.identity_and_access.domain.exception.PasswordNotInformed;
import com.springtodo.core.identity_and_access.domain.value_object.UserEmail;
import com.springtodo.core.identity_and_access.domain.value_object.UserId;
import com.springtodo.core.identity_and_access.domain.value_object.UserPassword;

public class UserBuilder {

    private UserId id = new UserId("default-id");
    private UserEmail email = new UserEmail("default@email.com");
    private UserPassword password = new UserPassword("defaultPassword");

    public UserBuilder withId(UserId id) {
        this.id = id;
        return this;
    }

    public UserBuilder withEmail(UserEmail email) {
        this.email = email;
        return this;
    }

    public UserBuilder withPassword(UserPassword password) {
        this.password = password;
        return this;
    }

    public User build() throws EmailNotInformed, PasswordNotInformed, IdNotInformed {
        return new User(id, email, password);
    }
}

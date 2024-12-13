package com.springtodo.core.identity_and_access.domain.value_object;

import java.io.Serializable;
import java.util.UUID;

import lombok.Getter;

@Getter
public class UserId implements Serializable {

    private String id;

    public UserId(String id) {
        this.id = id;
    }

    public UserId() {
        this.generateId();
    }

    @Override
    public String toString() {
        return "UserId{" + "id='" + id + '\'' + '}';
    }

    private void generateId() {
        this.id = UUID.randomUUID().toString();
    }
}

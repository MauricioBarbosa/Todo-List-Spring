package com.springtodo.core.identity_and_access.domain.value_object;

import java.io.Serializable;
import java.util.UUID;

import lombok.Getter;

@Getter
public class SessionId implements Serializable {

    private String id;

    public SessionId(String id) {
        this.id = id;
    }

    public SessionId() {
        this.generateId();
    }

    @Override
    public String toString() {
        return "SessionId{" + "id='" + id + '\'' + '}';
    }

    private void generateId() {
        this.id = UUID.randomUUID().toString();
    }
}

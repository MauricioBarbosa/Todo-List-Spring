package com.springtodo.core.autentication.domain.value_object;

import lombok.Getter;

@Getter
public class UserId {
    private String id;

    public UserId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserId{" +
                "id='" + id + '\'' +
                '}';
    }
}

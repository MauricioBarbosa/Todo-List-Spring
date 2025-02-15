package com.springtodo.core.identity_and_access.domain.value_object;

import lombok.Getter;

@Getter
public class SessionPermission {

    public static enum Permissions {
        CREATE_TASK,
        DELETE_TASK,
        READ_TASK,
        UPDATE_TASK,
        READ_SHARED_TASK,
        SHARE_TASK,
        CHANGE_USER_PASSWORD;
    }

    private final Permissions permissions[];

    private SessionPermission(Permissions[] permissions) {
        this.permissions = permissions;
    }

    public static SessionPermission createUserPermissions() {
        Permissions[] permissions = { Permissions.CREATE_TASK, Permissions.DELETE_TASK, Permissions.READ_TASK,
                Permissions.UPDATE_TASK, Permissions.READ_SHARED_TASK, Permissions.SHARE_TASK };

        return new SessionPermission(permissions);
    }

    public static SessionPermission createWithChangePasswordPermissions() {
        Permissions[] permissions = { Permissions.CHANGE_USER_PASSWORD };

        return new SessionPermission(permissions);
    }

    @Override
    public String toString() {
        return "{ " + this.permissions + " }";
    }

}

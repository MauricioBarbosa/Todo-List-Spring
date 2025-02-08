package com.springtodo.core.identity_and_access.domain.entity;

import java.io.Serializable;

import com.springtodo.core.identity_and_access.domain.exception.ConfirmationCodeIsNotEqualToSessionConfirmationCode;
import com.springtodo.core.identity_and_access.domain.value_object.ConfirmationCode;
import com.springtodo.core.identity_and_access.domain.value_object.SessionDuration;
import com.springtodo.core.identity_and_access.domain.value_object.SessionId;
import com.springtodo.core.identity_and_access.domain.value_object.SessionPermission;
import com.springtodo.core.identity_and_access.domain.value_object.SessionStatus;
import com.springtodo.core.identity_and_access.domain.value_object.SessionStatusConfirmated;
import com.springtodo.core.identity_and_access.domain.value_object.SessionStatusStarted;
import com.springtodo.core.identity_and_access.domain.value_object.UserId;

import lombok.Getter;

@Getter
public class Session implements Serializable {

    private SessionId sessionId;
    private UserId userId;
    private SessionDuration duration;
    private SessionStatus status;
    private ConfirmationCode confirmationCode;
    private SessionPermission permissions;
    private boolean isDetroyed;

    public Session(
            User user,
            Long durationInSeconds,
            int confirmationCodeSize,
            SessionPermission permissions) {
        this.userId = user.getId();
        this.duration = new SessionDuration(durationInSeconds);

        this.sessionId = new SessionId();
        this.status = new SessionStatusStarted();
        this.confirmationCode = new ConfirmationCode(confirmationCodeSize);
        this.permissions = permissions;
    }

    public Session(
            SessionId sessionId,
            UserId userId,
            SessionDuration duration,
            SessionStatus status,
            ConfirmationCode confirmationCode) {
        this.confirmationCode = confirmationCode;
        this.userId = userId;
        this.duration = duration;
        this.status = status;
        this.sessionId = sessionId;

        this.isDetroyed = false;
    }

    public SessionPermission.Permissions[] getPermissions() {
        return permissions.getPermissions();
    }

    public boolean isConfirmated() {
        return this.status.getClass() == SessionStatusConfirmated.class;
    }

    public void destroy() {
        this.isDetroyed = true;
    }

    public boolean isValidSession() {
        return !this.isDetroyed && this.isConfirmated();
    }

    public void confirm(ConfirmationCode aConfirmationCode)
            throws ConfirmationCodeIsNotEqualToSessionConfirmationCode {
        if (!this.confirmationCode.equals(aConfirmationCode)) {
            throw new ConfirmationCodeIsNotEqualToSessionConfirmationCode(
                    aConfirmationCode.getCode());
        }

        this.status = new SessionStatusConfirmated();
    }

    public static Session startChangePasswordSession(User user,
            Long durationInSeconds,
            int confirmationCodeSize) {

        return new Session(user, durationInSeconds, confirmationCodeSize,
                SessionPermission.createWithChangePasswordPermissions());
    }

    public static Session startUserSession(User user,
            Long durationInSeconds,
            int confirmationCodeSize) {

        return new Session(user, durationInSeconds, confirmationCodeSize, SessionPermission.createUserPermissions());
    }

    @Override
    public String toString() {
        return "Session{" +
                "sessionId=" + sessionId +
                ", userId=" + userId +
                ", duration=" + duration +
                ", status=" + status +
                ", confirmationCode=" + confirmationCode +
                ", permissions=" + permissions +
                '}';
    }

}

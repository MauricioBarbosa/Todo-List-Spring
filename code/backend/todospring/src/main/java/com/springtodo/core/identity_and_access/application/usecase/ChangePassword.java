package com.springtodo.core.identity_and_access.application.usecase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springtodo.core.identity_and_access.application.dto.ChangePasswordInput;
import com.springtodo.core.identity_and_access.application.exception.CouldNotDecodeToken;
import com.springtodo.core.identity_and_access.application.exception.InvalidToken;
import com.springtodo.core.identity_and_access.application.utils.SessionTokenGeneratorUtil;
import com.springtodo.core.identity_and_access.domain.entity.Session;
import com.springtodo.core.identity_and_access.domain.entity.User;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotFindSession;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSaveUser;
import com.springtodo.core.identity_and_access.domain.exception.InvalidPassword;
import com.springtodo.core.identity_and_access.domain.exception.NewPasswordShouldNotEqualsToPreviousPassword;
import com.springtodo.core.identity_and_access.domain.exception.OldPasswordDoesNotEqualToUserPassword;
import com.springtodo.core.identity_and_access.domain.exception.SessionNotFound;
import com.springtodo.core.identity_and_access.domain.exception.UserNotFoundException;
import com.springtodo.core.identity_and_access.domain.repository.SessionRepository;
import com.springtodo.core.identity_and_access.domain.repository.UserRepository;
import com.springtodo.core.identity_and_access.domain.value_object.SessionId;
import com.springtodo.core.identity_and_access.domain.value_object.UserPassword;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChangePassword {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionTokenGeneratorUtil sessionTokenGeneratorUtil;

    public void execute(ChangePasswordInput changePasswordInput)
            throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession, UserNotFoundException,
            CouldNotRetrieveUser, InvalidPassword, OldPasswordDoesNotEqualToUserPassword,
            NewPasswordShouldNotEqualsToPreviousPassword, CouldNotSaveUser {
        SessionId sessionId = sessionTokenGeneratorUtil.decode(changePasswordInput.getSessionToken());

        UserPassword newUserPassword = UserPassword.create(changePasswordInput.getNewPassword());
        UserPassword oldUserPassword = UserPassword.create(changePasswordInput.getOldPassword());

        Session session = sessionRepository.get(sessionId);

        if (!session.isValidSession()) {
            throw new InvalidToken();
        }

        User user = userRepository.getUserById(session.getUserId());

        if (!user.passwordEquals(oldUserPassword)) {
            throw new OldPasswordDoesNotEqualToUserPassword();
        }

        user.changePasswordTo(newUserPassword);

        userRepository.save(user);
    }
}

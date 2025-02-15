package com.springtodo.unit.core.identity_and_access.application.usecase;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.springtodo.core.identity_and_access.application.dto.ChangePasswordInput;
import com.springtodo.core.identity_and_access.application.exception.CouldNotDecodeToken;
import com.springtodo.core.identity_and_access.application.exception.InvalidToken;
import com.springtodo.core.identity_and_access.application.usecase.ChangePassword;
import com.springtodo.core.identity_and_access.application.utils.SessionTokenGeneratorUtil;
import com.springtodo.core.identity_and_access.domain.entity.Session;
import com.springtodo.core.identity_and_access.domain.entity.User;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotFindSession;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSaveUser;
import com.springtodo.core.identity_and_access.domain.exception.InvalidPassword;
import com.springtodo.core.identity_and_access.domain.exception.NewPasswordShouldNotEqualsToPreviousPassword;
import com.springtodo.core.identity_and_access.domain.exception.SessionNotFound;
import com.springtodo.core.identity_and_access.domain.exception.UserNotFoundException;
import com.springtodo.core.identity_and_access.domain.repository.SessionRepository;
import com.springtodo.core.identity_and_access.domain.repository.UserRepository;
import com.springtodo.core.identity_and_access.domain.value_object.SessionId;
import com.springtodo.core.identity_and_access.domain.value_object.UserId;

@ExtendWith(MockitoExtension.class)
public class ChangePasswordUnitTest {
    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SessionTokenGeneratorUtil sessionTokenGeneratorUtil;

    @InjectMocks
    private ChangePassword changePassword;

    @Test
    public void it_shouldThrowInvalidPassword() {
        String newPassword = "someinvalidpassword";
        String oldPassword = "someinvalidpassword";
        String sessionToken = "someSessionToken";

        ChangePasswordInput changePasswordInput = new ChangePasswordInput();

        changePasswordInput.setNewPassword(newPassword);
        changePasswordInput.setOldPassword(oldPassword);
        changePasswordInput.setSessionToken(sessionToken);

        assertThrows(InvalidPassword.class, () -> {
            changePassword.execute(changePasswordInput);
        });
    }

    @Test
    public void it_shouldThrowInvalidToken() throws InvalidToken, CouldNotDecodeToken {
        String newPassword = "Abc123@XYZ!";
        String oldPassword = "Abc123@XYZ!x";
        String sessionToken = "someSessionToken";

        InvalidToken invalidToken = new InvalidToken();

        ChangePasswordInput changePasswordInput = new ChangePasswordInput();

        changePasswordInput.setNewPassword(newPassword);
        changePasswordInput.setOldPassword(oldPassword);
        changePasswordInput.setSessionToken(sessionToken);

        when(sessionTokenGeneratorUtil.decode(any(String.class))).thenThrow(invalidToken);

        assertThrows(InvalidToken.class, () -> {
            changePassword.execute(changePasswordInput);
        });
    }

    @Test
    public void it_shouldThrowCouldNotDecodeToken() throws InvalidToken, CouldNotDecodeToken {
        String newPassword = "Abc123@XYZ!";
        String oldPassword = "Abc123@XYZ!x";
        String sessionToken = "someSessionToken";

        CouldNotDecodeToken couldNotDecodeToken = new CouldNotDecodeToken("some error message");

        ChangePasswordInput changePasswordInput = new ChangePasswordInput();

        changePasswordInput.setNewPassword(newPassword);
        changePasswordInput.setOldPassword(oldPassword);
        changePasswordInput.setSessionToken(sessionToken);

        when(sessionTokenGeneratorUtil.decode(any(String.class))).thenThrow(couldNotDecodeToken);

        assertThrows(CouldNotDecodeToken.class, () -> {
            changePassword.execute(changePasswordInput);
        });
    }

    @Test
    public void it_shouldThrowSessionNotFound()
            throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession {
        String newPassword = "Abc123@XYZ!";
        String oldPassword = "Abc123@XYZ!x";
        String sessionToken = "someSessionToken";

        ChangePasswordInput changePasswordInput = new ChangePasswordInput();

        changePasswordInput.setNewPassword(newPassword);
        changePasswordInput.setOldPassword(oldPassword);
        changePasswordInput.setSessionToken(sessionToken);

        Session session = Session.startChangePasswordSession(User.create("someEmail", "somePassword"), Long.valueOf(10),
                10);

        SessionId sessionId = session.getSessionId();

        SessionNotFound sessionNotFound = new SessionNotFound(sessionId);

        when(sessionTokenGeneratorUtil.decode(any(String.class))).thenReturn(session.getSessionId());

        when(sessionRepository.get((any(SessionId.class)))).thenThrow(sessionNotFound);

        assertThrows(SessionNotFound.class, () -> {
            changePassword.execute(changePasswordInput);
        });
    }

    @Test
    public void it_shouldThrowCouldNotFindSession()
            throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession {
        String newPassword = "Abc123@XYZ!";
        String oldPassword = "Abc123@XYZ!x";
        String sessionToken = "someSessionToken";

        ChangePasswordInput changePasswordInput = new ChangePasswordInput();

        changePasswordInput.setNewPassword(newPassword);
        changePasswordInput.setOldPassword(oldPassword);
        changePasswordInput.setSessionToken(sessionToken);

        Session session = Session.startChangePasswordSession(User.create("someEmail", "somePassword"), Long.valueOf(10),
                10);

        CouldNotFindSession couldNotFindSession = new CouldNotFindSession("could not find session for some reason");

        when(sessionTokenGeneratorUtil.decode(any(String.class))).thenReturn(session.getSessionId());

        when(sessionRepository.get((any(SessionId.class)))).thenThrow(couldNotFindSession);

        assertThrows(CouldNotFindSession.class, () -> {
            changePassword.execute(changePasswordInput);
        });
    }

    @Test
    public void it_shouldThrowUserNotFoundException() throws UserNotFoundException, CouldNotRetrieveUser,
            SessionNotFound, CouldNotFindSession, InvalidToken, CouldNotDecodeToken {
        String newPassword = "Abc123@XYZ!";
        String oldPassword = "Abc123@XYZ!x";
        String sessionToken = "someSessionToken";

        ChangePasswordInput changePasswordInput = new ChangePasswordInput();

        changePasswordInput.setNewPassword(newPassword);
        changePasswordInput.setOldPassword(oldPassword);
        changePasswordInput.setSessionToken(sessionToken);

        Session session = Session.startChangePasswordSession(User.create("someEmail", "somePassword"), Long.valueOf(10),
                10);

        UserNotFoundException userNotFoundException = new UserNotFoundException(("user not found for some reason"));

        when(sessionTokenGeneratorUtil.decode(any(String.class))).thenReturn(session.getSessionId());

        when(sessionRepository.get((any(SessionId.class)))).thenReturn(session);

        when(userRepository.getUserById(any(UserId.class))).thenThrow(userNotFoundException);

        assertThrows(UserNotFoundException.class, () -> {
            changePassword.execute(changePasswordInput);
        });
    }

    @Test
    public void it_shouldThrowCouldNotRetrieveUser() throws InvalidToken, CouldNotDecodeToken, SessionNotFound,
            CouldNotFindSession, UserNotFoundException, CouldNotRetrieveUser {
        String newPassword = "Abc123@XYZ!";
        String oldPassword = "Abc123@XYZ!x";
        String sessionToken = "someSessionToken";

        ChangePasswordInput changePasswordInput = new ChangePasswordInput();

        changePasswordInput.setNewPassword(newPassword);
        changePasswordInput.setOldPassword(oldPassword);
        changePasswordInput.setSessionToken(sessionToken);

        User user = User.create("someEmail", "somePassword");

        Session session = Session.startChangePasswordSession(user, Long.valueOf(10),
                10);

        CouldNotRetrieveUser couldNotRetrieveUser = new CouldNotRetrieveUser("could not retrieve user for some reason");

        when(sessionTokenGeneratorUtil.decode(any(String.class))).thenReturn(session.getSessionId());

        when(sessionRepository.get((any(SessionId.class)))).thenReturn(session);

        when(userRepository.getUserById(any(UserId.class))).thenThrow(couldNotRetrieveUser);

        assertThrows(CouldNotRetrieveUser.class, () -> {
            changePassword.execute(changePasswordInput);
        });
    }

    @Test
    public void it_shouldThrowNewPasswordShouldNotEqualsToPreviousPassword() throws InvalidToken, CouldNotDecodeToken,
            SessionNotFound, CouldNotFindSession, UserNotFoundException, CouldNotRetrieveUser {
        String newPassword = "Abc123@XYZ!";
        String oldPassword = "Abc123@XYZ!";
        String sessionToken = "someSessionToken";

        ChangePasswordInput changePasswordInput = new ChangePasswordInput();

        changePasswordInput.setNewPassword(newPassword);
        changePasswordInput.setOldPassword(oldPassword);
        changePasswordInput.setSessionToken(sessionToken);

        User user = User.create("someEmail", "Abc123@XYZ!");

        Session session = Session.startChangePasswordSession(user, Long.valueOf(10),
                10);

        when(sessionTokenGeneratorUtil.decode(any(String.class))).thenReturn(session.getSessionId());

        when(sessionRepository.get((any(SessionId.class)))).thenReturn(session);

        when(userRepository.getUserById(any(UserId.class))).thenReturn(user);

        assertThrows(NewPasswordShouldNotEqualsToPreviousPassword.class, () -> {
            changePassword.execute(changePasswordInput);
        });
    }

    @Test
    public void it_shouldThrowCouldNotSaveUser() throws UserNotFoundException, CouldNotRetrieveUser, SessionNotFound,
            CouldNotFindSession, InvalidToken, CouldNotDecodeToken, CouldNotSaveUser {
        String newPassword = "Abc123@XYZ!";
        String oldPassword = "Abc123@XYZ!x";
        String sessionToken = "someSessionToken";

        ChangePasswordInput changePasswordInput = new ChangePasswordInput();

        changePasswordInput.setNewPassword(newPassword);
        changePasswordInput.setOldPassword(oldPassword);
        changePasswordInput.setSessionToken(sessionToken);

        User user = User.create("someEmail", "Abc123@XYZ!x");

        Session session = Session.startChangePasswordSession(user, Long.valueOf(10),
                10);

        CouldNotSaveUser couldNotSaveUser = new CouldNotSaveUser("Could not save user for some reason");

        when(sessionTokenGeneratorUtil.decode(any(String.class))).thenReturn(session.getSessionId());

        when(sessionRepository.get((any(SessionId.class)))).thenReturn(session);

        when(userRepository.getUserById(any(UserId.class))).thenReturn(user);

        doThrow(couldNotSaveUser).when(userRepository).save(any(User.class));

        assertThrows(CouldNotSaveUser.class, () -> {
            changePassword.execute(changePasswordInput);
        });
    }

    @Test
    void it_shouldRunsWithoutErrors() throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession,
            UserNotFoundException, CouldNotRetrieveUser, CouldNotSaveUser {
        String newPassword = "Abc123@XYZ!";
        String oldPassword = "Abc123@XYZ!x";
        String sessionToken = "someSessionToken";

        ChangePasswordInput changePasswordInput = new ChangePasswordInput();

        changePasswordInput.setNewPassword(newPassword);
        changePasswordInput.setOldPassword(oldPassword);
        changePasswordInput.setSessionToken(sessionToken);

        User user = User.create("someEmail", "Abc123@XYZ!x");

        Session session = Session.startChangePasswordSession(user, Long.valueOf(10),
                10);

        when(sessionTokenGeneratorUtil.decode(any(String.class))).thenReturn(session.getSessionId());

        when(sessionRepository.get((any(SessionId.class)))).thenReturn(session);

        when(userRepository.getUserById(any(UserId.class))).thenReturn(user);

        doNothing().when(userRepository).save(any(User.class));

        assertAll(() -> {
            changePassword.execute(changePasswordInput);
        });
    }
}

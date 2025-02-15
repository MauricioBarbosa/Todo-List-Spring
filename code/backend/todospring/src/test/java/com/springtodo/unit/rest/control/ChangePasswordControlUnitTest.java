package com.springtodo.unit.rest.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.springtodo.core.identity_and_access.application.dto.ChangePasswordInput;
import com.springtodo.core.identity_and_access.application.exception.CouldNotDecodeToken;
import com.springtodo.core.identity_and_access.application.exception.InvalidToken;
import com.springtodo.core.identity_and_access.application.usecase.ChangePassword;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotFindSession;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSaveUser;
import com.springtodo.core.identity_and_access.domain.exception.InvalidPassword;
import com.springtodo.core.identity_and_access.domain.exception.NewPasswordShouldNotEqualsToPreviousPassword;
import com.springtodo.core.identity_and_access.domain.exception.OldPasswordDoesNotEqualToUserPassword;
import com.springtodo.core.identity_and_access.domain.exception.SessionNotFound;
import com.springtodo.core.identity_and_access.domain.exception.UserNotFoundException;
import com.springtodo.core.identity_and_access.domain.value_object.SessionId;
import com.springtodo.rest.control.ChangePasswordControl;
import com.springtodo.rest.pojo.change_password_control.ChangePasswordRequestBody;

@ExtendWith(MockitoExtension.class)
public class ChangePasswordControlUnitTest {
    @Mock
    private ChangePassword changePasswordUseCaseMock;

    @InjectMocks
    private ChangePasswordControl changePasswordControl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void it_shouldThrowInvalidToken() throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession,
            UserNotFoundException, CouldNotRetrieveUser, InvalidPassword, OldPasswordDoesNotEqualToUserPassword,
            NewPasswordShouldNotEqualsToPreviousPassword, CouldNotSaveUser {
        InvalidToken invalidToken = new InvalidToken();
        String sessionToken = "someSessionToken";
        ChangePasswordRequestBody changePasswordRequestBody = new ChangePasswordRequestBody();

        changePasswordRequestBody.setOldPassword("someOldPassword");
        changePasswordRequestBody.setNewPassword("someNewPassword");

        doThrow(invalidToken).when(changePasswordUseCaseMock)
                .execute(any(ChangePasswordInput.class));

        assertThrows(InvalidToken.class, () -> {
            changePasswordControl.changePassword(sessionToken, changePasswordRequestBody);
        });
    }

    @Test
    void it_shouldThrowCouldNotDecodeToken() throws InvalidToken, CouldNotDecodeToken, SessionNotFound,
            CouldNotFindSession, UserNotFoundException, CouldNotRetrieveUser, InvalidPassword,
            OldPasswordDoesNotEqualToUserPassword, NewPasswordShouldNotEqualsToPreviousPassword, CouldNotSaveUser {
        CouldNotDecodeToken couldNotDecodeToken = new CouldNotDecodeToken("some error has occurred");
        String sessionToken = "someSessionToken";
        ChangePasswordRequestBody changePasswordRequestBody = new ChangePasswordRequestBody();

        changePasswordRequestBody.setOldPassword("someOldPassword");
        changePasswordRequestBody.setNewPassword("someNewPassword");

        doThrow(couldNotDecodeToken).when(changePasswordUseCaseMock)
                .execute(any(ChangePasswordInput.class));

        assertThrows(CouldNotDecodeToken.class, () -> {
            changePasswordControl.changePassword(sessionToken, changePasswordRequestBody);
        });
    }

    @Test
    void it_shouldThrowSessionNotFound() throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession,
            UserNotFoundException, CouldNotRetrieveUser, InvalidPassword, OldPasswordDoesNotEqualToUserPassword,
            NewPasswordShouldNotEqualsToPreviousPassword, CouldNotSaveUser {
        SessionNotFound sessionNotFound = new SessionNotFound(new SessionId());
        String sessionToken = "someSessionToken";
        ChangePasswordRequestBody changePasswordRequestBody = new ChangePasswordRequestBody();

        changePasswordRequestBody.setOldPassword("someOldPassword");
        changePasswordRequestBody.setNewPassword("someNewPassword");

        doThrow(sessionNotFound).when(changePasswordUseCaseMock)
                .execute(any(ChangePasswordInput.class));

        assertThrows(SessionNotFound.class, () -> {
            changePasswordControl.changePassword(sessionToken, changePasswordRequestBody);
        });
    }

    @Test
    void it_shouldThrowCouldNotFindSession() throws InvalidToken, CouldNotDecodeToken, SessionNotFound,
            CouldNotFindSession, UserNotFoundException, CouldNotRetrieveUser, InvalidPassword,
            OldPasswordDoesNotEqualToUserPassword, NewPasswordShouldNotEqualsToPreviousPassword, CouldNotSaveUser {
        CouldNotFindSession couldNotFindSession = new CouldNotFindSession("some error message");
        String sessionToken = "someSessionToken";
        ChangePasswordRequestBody changePasswordRequestBody = new ChangePasswordRequestBody();

        changePasswordRequestBody.setOldPassword("someOldPassword");
        changePasswordRequestBody.setNewPassword("someNewPassword");

        doThrow(couldNotFindSession).when(changePasswordUseCaseMock)
                .execute(any(ChangePasswordInput.class));

        assertThrows(CouldNotFindSession.class, () -> {
            changePasswordControl.changePassword(sessionToken, changePasswordRequestBody);
        });
    }

    @Test
    void it_shouldThrowUserNotFoundException() throws InvalidToken, CouldNotDecodeToken, SessionNotFound,
            CouldNotFindSession, UserNotFoundException, CouldNotRetrieveUser, InvalidPassword,
            OldPasswordDoesNotEqualToUserPassword, NewPasswordShouldNotEqualsToPreviousPassword, CouldNotSaveUser {
        UserNotFoundException userNotFoundException = new UserNotFoundException("some user not found exception");
        String sessionToken = "someSessionToken";
        ChangePasswordRequestBody changePasswordRequestBody = new ChangePasswordRequestBody();

        changePasswordRequestBody.setOldPassword("someOldPassword");
        changePasswordRequestBody.setNewPassword("someNewPassword");

        doThrow(userNotFoundException).when(changePasswordUseCaseMock)
                .execute(any(ChangePasswordInput.class));

        assertThrows(UserNotFoundException.class, () -> {
            changePasswordControl.changePassword(sessionToken, changePasswordRequestBody);
        });
    }

    @Test
    void it_shouldThrowCouldNotRetrieveUser() throws InvalidToken, CouldNotDecodeToken, SessionNotFound,
            CouldNotFindSession, UserNotFoundException, CouldNotRetrieveUser, InvalidPassword,
            OldPasswordDoesNotEqualToUserPassword, NewPasswordShouldNotEqualsToPreviousPassword, CouldNotSaveUser {
        CouldNotRetrieveUser couldNotRetrieveUser = new CouldNotRetrieveUser("some user not found exception");
        String sessionToken = "someSessionToken";
        ChangePasswordRequestBody changePasswordRequestBody = new ChangePasswordRequestBody();

        changePasswordRequestBody.setOldPassword("someOldPassword");
        changePasswordRequestBody.setNewPassword("someNewPassword");

        doThrow(couldNotRetrieveUser).when(changePasswordUseCaseMock)
                .execute(any(ChangePasswordInput.class));

        assertThrows(CouldNotRetrieveUser.class, () -> {
            changePasswordControl.changePassword(sessionToken, changePasswordRequestBody);
        });
    }

    @Test
    void it_shouldThrowInvalidPassword() throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession,
            UserNotFoundException, CouldNotRetrieveUser, InvalidPassword, OldPasswordDoesNotEqualToUserPassword,
            NewPasswordShouldNotEqualsToPreviousPassword, CouldNotSaveUser {
        InvalidPassword invalidPassword = new InvalidPassword("some user not found exception");
        String sessionToken = "someSessionToken";
        ChangePasswordRequestBody changePasswordRequestBody = new ChangePasswordRequestBody();

        changePasswordRequestBody.setOldPassword("someOldPassword");
        changePasswordRequestBody.setNewPassword("someNewPassword");

        doThrow(invalidPassword).when(changePasswordUseCaseMock)
                .execute(any(ChangePasswordInput.class));

        assertThrows(InvalidPassword.class, () -> {
            changePasswordControl.changePassword(sessionToken, changePasswordRequestBody);
        });
    }

    @Test
    void it_shouldThrowOldPasswordDoesNotEqualToUserPassword() throws InvalidToken, CouldNotDecodeToken,
            SessionNotFound, CouldNotFindSession, UserNotFoundException, CouldNotRetrieveUser, InvalidPassword,
            OldPasswordDoesNotEqualToUserPassword, NewPasswordShouldNotEqualsToPreviousPassword, CouldNotSaveUser {
        OldPasswordDoesNotEqualToUserPassword oldPasswordDoesNotEqualToUserPassword = new OldPasswordDoesNotEqualToUserPassword();
        String sessionToken = "someSessionToken";
        ChangePasswordRequestBody changePasswordRequestBody = new ChangePasswordRequestBody();

        changePasswordRequestBody.setOldPassword("someOldPassword");
        changePasswordRequestBody.setNewPassword("someNewPassword");

        doThrow(oldPasswordDoesNotEqualToUserPassword).when(changePasswordUseCaseMock)
                .execute(any(ChangePasswordInput.class));

        assertThrows(OldPasswordDoesNotEqualToUserPassword.class, () -> {
            changePasswordControl.changePassword(sessionToken, changePasswordRequestBody);
        });
    }

    @Test
    void it_shouldThrowNewPasswordShouldNotEqualsToPreviousPassword() throws InvalidToken, CouldNotDecodeToken,
            SessionNotFound, CouldNotFindSession, UserNotFoundException, CouldNotRetrieveUser, InvalidPassword,
            OldPasswordDoesNotEqualToUserPassword, NewPasswordShouldNotEqualsToPreviousPassword, CouldNotSaveUser {
        NewPasswordShouldNotEqualsToPreviousPassword newPasswordShouldNotEqualsToPreviousPassword = new NewPasswordShouldNotEqualsToPreviousPassword();
        String sessionToken = "someSessionToken";
        ChangePasswordRequestBody changePasswordRequestBody = new ChangePasswordRequestBody();

        changePasswordRequestBody.setOldPassword("someOldPassword");
        changePasswordRequestBody.setNewPassword("someNewPassword");

        doThrow(newPasswordShouldNotEqualsToPreviousPassword).when(changePasswordUseCaseMock)
                .execute(any(ChangePasswordInput.class));

        assertThrows(NewPasswordShouldNotEqualsToPreviousPassword.class, () -> {
            changePasswordControl.changePassword(sessionToken, changePasswordRequestBody);
        });
    }

    @Test
    void it_shouldCouldNotSaveUser() throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession,
            UserNotFoundException, CouldNotRetrieveUser, InvalidPassword, OldPasswordDoesNotEqualToUserPassword,
            NewPasswordShouldNotEqualsToPreviousPassword, CouldNotSaveUser {
        CouldNotSaveUser couldNotSaveUser = new CouldNotSaveUser("some error message");
        String sessionToken = "someSessionToken";
        ChangePasswordRequestBody changePasswordRequestBody = new ChangePasswordRequestBody();

        changePasswordRequestBody.setOldPassword("someOldPassword");
        changePasswordRequestBody.setNewPassword("someNewPassword");

        doThrow(couldNotSaveUser).when(changePasswordUseCaseMock).execute(any(ChangePasswordInput.class));

        assertThrows(CouldNotSaveUser.class, () -> {
            changePasswordControl.changePassword(sessionToken, changePasswordRequestBody);
        });
    }

    @Test
    void it_shouldReturn200Status() throws InvalidToken, CouldNotDecodeToken, SessionNotFound, CouldNotFindSession,
            UserNotFoundException, CouldNotRetrieveUser, InvalidPassword, OldPasswordDoesNotEqualToUserPassword,
            NewPasswordShouldNotEqualsToPreviousPassword, CouldNotSaveUser {
        String sessionToken = "someSessionToken";
        ChangePasswordRequestBody changePasswordRequestBody = new ChangePasswordRequestBody();

        changePasswordRequestBody.setOldPassword("someOldPassword");
        changePasswordRequestBody.setNewPassword("someNewPassword");

        doNothing().when(changePasswordUseCaseMock).execute(any(ChangePasswordInput.class));

        ResponseEntity<Void> response = changePasswordControl.changePassword(sessionToken, changePasswordRequestBody);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
}

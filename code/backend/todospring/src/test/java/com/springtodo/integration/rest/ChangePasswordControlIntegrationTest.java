package com.springtodo.integration.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springtodo.App;
import com.springtodo.core.identity_and_access.application.exception.CouldNotGenerateToken;
import com.springtodo.core.identity_and_access.application.utils.SessionTokenGeneratorUtil;
import com.springtodo.core.identity_and_access.domain.entity.Session;
import com.springtodo.core.identity_and_access.domain.entity.User;
import com.springtodo.core.identity_and_access.domain.exception.ConfirmationCodeIsNotEqualToSessionConfirmationCode;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSaveSession;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSaveUser;
import com.springtodo.core.identity_and_access.domain.repository.SessionRepository;
import com.springtodo.core.identity_and_access.domain.repository.UserRepository;
import com.springtodo.core.identity_and_access.domain.value_object.ConfirmationCode;
import com.springtodo.rest.pojo.change_password_control.ChangePasswordRequestBody;

import groovy.util.logging.Log4j;
import jakarta.transaction.Transactional;

@SpringBootTest(classes = App.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@EnableCaching
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
@Log4j
public class ChangePasswordControlIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private SessionRepository sessionRepository;

        @Autowired
        private SessionTokenGeneratorUtil sessionTokenGeneratorUtil;

        @Value("${jwtSecretKey}")
        private String secretKey;

        @Value("${sessionDurationInSeconds}")
        private Long sessionDurationInSeconds;

        @Value("${confirmationCodeSize}")
        private int confirmationCodeSize;

        private User user;

        private Session session;

        private String sessionToken;

        @BeforeEach()
        void setUp() throws CouldNotSaveUser, ConfirmationCodeIsNotEqualToSessionConfirmationCode,
                        CouldNotGenerateToken, CouldNotSaveSession {
                user = User.create("userEmail@email.com", "Abcdefg1!X");

                userRepository.save(user);

                session = Session.startChangePasswordSession(user, sessionDurationInSeconds, confirmationCodeSize);

                sessionRepository.save(session);

                ConfirmationCode confirmationCode = session.getConfirmationCode();

                session.confirm(confirmationCode);

                sessionToken = sessionTokenGeneratorUtil.generate(session.getSessionId().getId(),
                                session.getDuration().getStart(),
                                session.getDuration().getEnd());
        }

        @Test
        void it_shouldReturnBadRequestOnceSessionTokenWasNotInformed() throws JsonProcessingException, Exception {
                ChangePasswordRequestBody changePasswordRequestBody = new ChangePasswordRequestBody();

                ArrayList<String> messages = new ArrayList<String>();

                messages.add("Required header 'sessionToken' is not present.");

                mockMvc.perform(
                                post("/change-password")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(new ObjectMapper()
                                                                .writeValueAsString(changePasswordRequestBody)))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.messages").isArray())
                                .andExpect(jsonPath("$.messages").value(messages));
        }

        @Test
        void it_shouldReturnBadRequestOnceOldPasswordWasNotInformed() throws JsonProcessingException, Exception {
                ChangePasswordRequestBody changePasswordRequestBody = new ChangePasswordRequestBody();

                ArrayList<String> messages = new ArrayList<String>();

                messages.add("oldPassword must be informed");
                messages.add("newPassword must be informed");

                mockMvc.perform(
                                post("/change-password")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header("sessionToken", sessionToken)
                                                .content(new ObjectMapper()
                                                                .writeValueAsString(
                                                                                changePasswordRequestBody)))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.messages").isArray());
        }

        @Test
        void it_shouldReturnBadRequestOnceNewPasswordWasNotInformed() throws JsonProcessingException, Exception {
                ChangePasswordRequestBody changePasswordRequestBody = new ChangePasswordRequestBody();
                changePasswordRequestBody.setOldPassword("someOldPassword");

                ArrayList<String> messages = new ArrayList<String>();

                messages.add("newPassword must be informed");

                mockMvc.perform(
                                post("/change-password")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .header("sessionToken", sessionToken)
                                                .content(new ObjectMapper()
                                                                .writeValueAsString(changePasswordRequestBody)))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.messages").isArray())
                                .andExpect(jsonPath("$.messages").value(messages));
        }

        @Test
        void it_shouldReturnStatusUnauthorizedOnceTokenIsInvalid() throws JsonProcessingException, Exception {
                ChangePasswordRequestBody changePasswordRequestBody = new ChangePasswordRequestBody();
                changePasswordRequestBody.setOldPassword("someOldPassword");
                changePasswordRequestBody.setNewPassword("someNewPassword");

                ArrayList<String> messages = new ArrayList<String>();

                messages.add("invalid token");

                mockMvc.perform(
                                post("/change-password")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(new ObjectMapper()
                                                                .writeValueAsString(
                                                                                changePasswordRequestBody))
                                                .header("sessionToken", "someInvalidtoken"))
                                .andExpect(status().isUnauthorized())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.messages").isArray())
                                .andExpect(jsonPath("$.messages").value(messages));
        }

        // test invalid password

        @Test
        void it_shouldReturnStatusUnauthorizedOnceSessionWasNotFound() throws JsonProcessingException, Exception {
                ChangePasswordRequestBody changePasswordRequestBody = new ChangePasswordRequestBody();
                changePasswordRequestBody.setOldPassword("Abcdefg1!X");
                changePasswordRequestBody.setNewPassword("XyZ12345@b");

                ArrayList<String> messages = new ArrayList<String>();

                String fakeSessionToken = sessionTokenGeneratorUtil.generate("someFakeSessionToken",
                                session.getDuration().getStart(), session.getDuration().getEnd());

                messages.add("Session not found");

                mockMvc.perform(
                                post("/change-password")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(new ObjectMapper()
                                                                .writeValueAsString(
                                                                                changePasswordRequestBody))
                                                .header("sessionToken", fakeSessionToken))
                                .andExpect(status().isUnauthorized())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.messages").isArray())
                                .andExpect(jsonPath("$.messages").value(messages));
        }

        @Test
        void it_shouldReturnStatusBadRequestOnceOldPasswordDoesNotEqualToUserPassword()
                        throws JsonProcessingException, Exception {
                ChangePasswordRequestBody changePasswordRequestBody = new ChangePasswordRequestBody();
                changePasswordRequestBody.setOldPassword("MnpqrSt1$z");
                changePasswordRequestBody.setNewPassword("XyZ12345@b");

                ArrayList<String> messages = new ArrayList<String>();

                messages.add("old password does not equal to user password");

                mockMvc.perform(
                                post("/change-password")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(new ObjectMapper()
                                                                .writeValueAsString(
                                                                                changePasswordRequestBody))
                                                .header("sessionToken", sessionToken))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.messages").isArray())
                                .andExpect(jsonPath("$.messages").value(messages));
        }

        @Test
        void it_shouldChangeUserPasswordAndDestroySession() throws JsonProcessingException, Exception {
                ChangePasswordRequestBody changePasswordRequestBody = new ChangePasswordRequestBody();

                changePasswordRequestBody.setOldPassword("Abcdefg1!X");
                changePasswordRequestBody.setNewPassword("XyZ12345@b");

                mockMvc.perform(
                                post("/change-password")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(new ObjectMapper()
                                                                .writeValueAsString(
                                                                                changePasswordRequestBody))
                                                .header("sessionToken", sessionToken))
                                .andExpect(status().isOk());

        }
}

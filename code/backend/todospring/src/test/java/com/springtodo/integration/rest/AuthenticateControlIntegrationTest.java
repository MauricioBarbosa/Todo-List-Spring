package com.springtodo.integration.rest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import javax.crypto.SecretKey;

import org.json.JSONObject;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springtodo.App;
import com.springtodo.core.identity_and_access.domain.entity.User;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSaveUser;
import com.springtodo.core.identity_and_access.domain.repository.UserRepository;
import com.springtodo.core.identity_and_access.domain.value_object.SessionPermission;
import com.springtodo.rest.pojo.authenticate.AuthenticateRequestBody;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@SpringBootTest(classes = App.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@EnableCaching
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class AuthenticateControlIntegrationTest {
        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private UserRepository userRepository;

        @Value("${jwtSecretKey}")
        private String secretKey;

        @Value("")
        private Long sessionDurationInSeconds;

        @Value("")
        private int confirmationCodeSize;

        private User user;

        @BeforeEach()
        void setUp() throws CouldNotSaveUser {
                user = User.create("userEmail@email.com", "Abcdefg1!X");

                userRepository.save(user);
        }

        @Test
        void it_shouldReturnBadRequestOnceEmailWasNotInformed() throws JsonProcessingException, Exception {
                AuthenticateRequestBody authenticateRequestBody = new AuthenticateRequestBody();

                ArrayList<String> messages = new ArrayList<String>();

                messages.add("email must be informed");
                messages.add("password must be informed");

                mockMvc.perform(
                                post("/change-password")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(new ObjectMapper()
                                                                .writeValueAsString(authenticateRequestBody)))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.messages").isArray())
                                .andExpect(jsonPath("$.messages").value(messages));
        }

        @Test
        void it_shouldReturnBadRequestOncePasswordWasNotInformed() throws JsonProcessingException, Exception {
                AuthenticateRequestBody authenticateRequestBody = new AuthenticateRequestBody();

                authenticateRequestBody.setEmail("someUserEmail@email.com");

                ArrayList<String> messages = new ArrayList<String>();

                messages.add("password must be informed");

                mockMvc.perform(
                                post("/change-password")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(new ObjectMapper()
                                                                .writeValueAsString(authenticateRequestBody)))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.messages").isArray())
                                .andExpect(jsonPath("$.messages").value(messages));
        }

        @Test
        void it_shouldReturnBadRequestOnceEmailWasInformedButIsNotEmail() throws JsonProcessingException, Exception {
                AuthenticateRequestBody authenticateRequestBody = new AuthenticateRequestBody();

                authenticateRequestBody.setEmail("mauricio");
                authenticateRequestBody.setPassword("mauricio");

                ArrayList<String> messages = new ArrayList<String>();

                messages.add("email informed is not an email");

                mockMvc.perform(
                                post("/change-password")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(new ObjectMapper()
                                                                .writeValueAsString(authenticateRequestBody)))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.messages").isArray())
                                .andExpect(jsonPath("$.messages").value(messages));
        }

        @Test
        void it_shouldReturnBadRequestOncePasswordIsWeak() throws JsonProcessingException, Exception {
                AuthenticateRequestBody authenticateRequestBody = new AuthenticateRequestBody();

                authenticateRequestBody.setEmail("mauricio@email.com.br");
                authenticateRequestBody.setPassword("someweakpassword");

                ArrayList<String> messages = new ArrayList<String>();

                messages.add("email informed is not an email");

                mockMvc.perform(
                                post("/change-password")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(new ObjectMapper()
                                                                .writeValueAsString(authenticateRequestBody)))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.messages").isArray())
                                .andExpect(jsonPath("$.messages").value(messages));
        }

        @Test
        void it_shouldReturnBadRequestOnceUserWasNotFound() throws JsonProcessingException, Exception {
                AuthenticateRequestBody authenticateRequestBody = new AuthenticateRequestBody();

                authenticateRequestBody.setEmail("mauricio@email.com.br");
                authenticateRequestBody.setPassword("Abc123@XYZ!");

                ArrayList<String> messages = new ArrayList<String>();

                messages.add("user not found");

                mockMvc.perform(
                                post("/change-password")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(new ObjectMapper()
                                                                .writeValueAsString(authenticateRequestBody)))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.messages").isArray())
                                .andExpect(jsonPath("$.messages").value(messages));
        }

        @Test
        void it_shouldReturnBadRequestOnceInformedPasswordNotEqualsToUserPassword()
                        throws JsonProcessingException, Exception {
                AuthenticateRequestBody authenticateRequestBody = new AuthenticateRequestBody();

                authenticateRequestBody.setEmail("mauricio@email.com.br");
                authenticateRequestBody.setPassword("Abc123@XYZ!");

                ArrayList<String> messages = new ArrayList<String>();

                messages.add("user not found");

                mockMvc.perform(
                                post("/change-password")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(new ObjectMapper()
                                                                .writeValueAsString(authenticateRequestBody)))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.messages").isArray())
                                .andExpect(jsonPath("$.messages").value(messages));
        }

        @Test
        void it_shouldReturnOkButWithUserConfirmedFalse() throws JsonProcessingException, Exception {
                AuthenticateRequestBody authenticateRequestBody = new AuthenticateRequestBody();

                authenticateRequestBody.setEmail("mauricio@email.com.br");
                authenticateRequestBody.setPassword("Abc123@XYZ!");

                MvcResult responseResult = mockMvc.perform(post("/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(
                                                authenticateRequestBody)))
                                .andExpect(status().isOk())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.sessionToken").isString()).andReturn();

                SecretKey secretKeyBytes = Keys.hmacShaKeyFor(secretKey.getBytes());

                JSONObject responseBody = new JSONObject(responseResult.getResponse().getContentAsString());

                String token = responseBody.getString("sessionToken");

                assertEquals(responseBody.getBoolean("isUserConfirmed"), false);
                assertEquals(responseBody.getBoolean("confirmated"), false);
                assertEquals(responseBody.getJSONArray("permissions").get(0), "CONFIRM_USER");
                assertEquals(responseBody.getString("expiresIn").getClass(), String.class);
                assertDoesNotThrow(() -> {
                        Jwts.parser().verifyWith(secretKeyBytes).build().parseSignedClaims(token).getPayload();
                });
        }

        @Test
        void it_shouldReturnOkButWithUserConfirmedTrue() throws JsonProcessingException, Exception {
                AuthenticateRequestBody authenticateRequestBody = new AuthenticateRequestBody();

                authenticateRequestBody.setEmail("mauricio@email.com.br");
                authenticateRequestBody.setPassword("Abc123@XYZ!");

                MvcResult responseResult = mockMvc.perform(post("/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(
                                                authenticateRequestBody)))
                                .andExpect(status().isOk())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.sessionToken").isString()).andReturn();

                SecretKey secretKeyBytes = Keys.hmacShaKeyFor(secretKey.getBytes());

                JSONObject responseBody = new JSONObject(responseResult.getResponse().getContentAsString());

                String token = responseBody.getString("sessionToken");

                ArrayList<SessionPermission.Permissions> permissions = new ArrayList<SessionPermission.Permissions>();
                permissions.add(SessionPermission.Permissions.CREATE_TASK);
                permissions.add(SessionPermission.Permissions.DELETE_TASK);
                permissions.add(SessionPermission.Permissions.READ_SHARED_TASK);
                permissions.add(SessionPermission.Permissions.READ_TASK);
                permissions.add(SessionPermission.Permissions.UPDATE_TASK);
                permissions.add(SessionPermission.Permissions.CHANGE_USER_PASSWORD);

                assertEquals(responseBody.getBoolean("isUserConfirmed"), false);
                assertEquals(responseBody.getBoolean("confirmated"), false);
                assertEquals(responseBody.getJSONArray("permissions"), permissions);
                assertEquals(responseBody.getString("expiresIn").getClass(), String.class);
                assertDoesNotThrow(() -> {
                        Jwts.parser().verifyWith(secretKeyBytes).build().parseSignedClaims(token).getPayload();
                });
        }
}

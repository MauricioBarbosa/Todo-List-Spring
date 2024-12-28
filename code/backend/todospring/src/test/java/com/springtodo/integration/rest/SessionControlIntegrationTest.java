package com.springtodo.integration.rest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import javax.crypto.SecretKey;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springtodo.App;
import com.springtodo.core.identity_and_access.application.utils.SessionTokenGeneratorUtil;
import com.springtodo.core.identity_and_access.domain.entity.Session;
import com.springtodo.core.identity_and_access.domain.repository.SessionRepository;
import com.springtodo.core.identity_and_access.domain.value_object.SessionDuration;
import com.springtodo.core.identity_and_access.domain.value_object.SessionId;
import com.springtodo.core.identity_and_access.domain.value_object.SessionStatusConfirmated;
import com.springtodo.core.identity_and_access.infrastructure.persistence.jpa.entity.UserJpa;
import com.springtodo.core.identity_and_access.infrastructure.persistence.jpa.repository.UserJpaRepository;
import com.springtodo.rest.pojo.session_control.ConfirmSessionInput;
import com.springtodo.rest.pojo.session_control.LoginInput;

import groovy.util.logging.Log4j;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;

@SpringBootTest(classes = App.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@EnableCaching
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
@Log4j
public class SessionControlIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private UserJpaRepository userJpaRepository;

        @Autowired
        private SessionRepository sessionRepository;

        @Autowired
        private SessionTokenGeneratorUtil sessionTokenGeneratorUtil;

        @Value("${jwtSecretKey}")
        private String secretKey;

        private String userName = "Some user";
        private String userEmail = "some@email1.com";
        private String userPassword = "#somePassword";
        private String userFullname = "Some full name";
        private String id = UUID.randomUUID().toString();

        @BeforeEach
        void setUp() {
                UserJpa userJpa = new UserJpa();

                userJpa.setId(UUID.fromString(id));
                userJpa.setName(this.userName);
                userJpa.setEmail(this.userEmail);
                userJpa.setPassword(this.userPassword);
                userJpa.setFullname(this.userFullname);

                userJpaRepository.save(userJpa);
        }

        @Nested
        class TestLogin {

                @Test
                void it_shouldReturnUserNotFoundWithStatus400() throws JsonProcessingException, Exception {
                        String aWrongEmail = "wrong.email@email.com";

                        LoginInput loginInput = new LoginInput();

                        loginInput.setEmail(aWrongEmail);
                        loginInput.setPassword(userPassword);

                        mockMvc.perform(
                                        post("/login")
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(new ObjectMapper()
                                                                        .writeValueAsString(loginInput)))
                                        .andExpect(status().isBadRequest())
                                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                        .andExpect(jsonPath("$.message").isString())
                                        .andExpect(jsonPath("$.message")
                                                        .value("user of email" + aWrongEmail + " not found"));
                }

                @Test
                void it_shouldReturnInvalidPasswordWithStatus400() throws JsonProcessingException, Exception {
                        String wrongPassword = "wrongpassword";

                        LoginInput loginInput = new LoginInput();

                        loginInput.setEmail(userEmail);
                        loginInput.setPassword(wrongPassword);

                        mockMvc.perform(
                                        post("/login")
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(new ObjectMapper()
                                                                        .writeValueAsString(loginInput)))
                                        .andExpect(status().isBadRequest())
                                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                        .andExpect(jsonPath("$.message").isString())
                                        .andExpect(jsonPath("$.message").value("user password doesn't match"));
                }

                @Test
                void it_shouldReturnASessionTokenWithStatus200() throws JsonProcessingException, Exception {
                        SecretKey secretKeyBytes = Keys.hmacShaKeyFor(secretKey.getBytes());

                        LoginInput loginInput = new LoginInput();

                        loginInput.setEmail(userEmail);
                        loginInput.setPassword(userPassword);

                        MvcResult responseResult = mockMvc.perform(post("/login")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(new ObjectMapper().writeValueAsString(
                                                        loginInput)))
                                        .andExpect(status().isOk())
                                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                        .andExpect(jsonPath("$.sessionToken").isString()).andReturn();

                        JSONObject responseBody = new JSONObject(responseResult.getResponse().getContentAsString());
                        String token = responseBody.getString("sessionToken");

                        assertDoesNotThrow(() -> {
                                Jwts.parser().verifyWith(secretKeyBytes).build().parseSignedClaims(token).getPayload();
                        });
                }
        }

        @Nested
        class TestConfirmSession {
                private String sessionToken;

                @BeforeEach
                void setUp() throws JsonProcessingException, Exception {

                        LoginInput loginInput = new LoginInput();

                        loginInput.setEmail(userEmail);
                        loginInput.setPassword(userPassword);

                        MvcResult responseResult = mockMvc.perform(post("/login")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(new ObjectMapper().writeValueAsString(
                                                        loginInput)))
                                        .andExpect(status().isOk())
                                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                        .andExpect(jsonPath("$.sessionToken").isString()).andReturn();

                        JSONObject responseBody = new JSONObject(responseResult.getResponse().getContentAsString());
                        String token = responseBody.getString("sessionToken");

                        this.sessionToken = token;
                }

                @Test
                void it_shouldReturnInvalidTokenWithStatus403() throws Exception {
                        String fakeToken = "someFakeToken";
                        String anyConfirmationCode = "anyConfirmationCode";

                        ConfirmSessionInput confirmSessionInput = new ConfirmSessionInput();
                        confirmSessionInput.setConfirmationCode(anyConfirmationCode);

                        mockMvc.perform(post("/confirm-session").header("sessionToken", fakeToken)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(new ObjectMapper().writeValueAsString(
                                                        confirmSessionInput)))
                                        .andExpect(status().isForbidden());
                }

                @Test
                void it_shouldReturnSessionNotFoundWithStatus401() throws Exception {
                        String fakeSessionId = "someFakeSessionId";

                        SessionDuration sessionDuration = new SessionDuration(Long.valueOf(120));

                        String fakeSessionToken = sessionTokenGeneratorUtil.generate(fakeSessionId,
                                        sessionDuration.getStart(),
                                        sessionDuration.getEnd());

                        String anyConfirmationCode = "anyConfirmationCode";

                        ConfirmSessionInput confirmSessionInput = new ConfirmSessionInput();

                        confirmSessionInput.setConfirmationCode(anyConfirmationCode);

                        mockMvc
                                        .perform(post("/confirm-session").header("sessionToken", fakeSessionToken)
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(new ObjectMapper().writeValueAsString(
                                                                        confirmSessionInput)))
                                        .andExpect(status().isUnauthorized()).andReturn();
                }

                @Test
                void it_shouldReturnConfirmationCodeAreNotEqualsWithStatus400()
                                throws JsonProcessingException, Exception {
                        String fakeConfirmationCode = "someFakeConfirmationCode";

                        ConfirmSessionInput confirmSessionInput = new ConfirmSessionInput();

                        confirmSessionInput.setConfirmationCode(fakeConfirmationCode);

                        mockMvc.perform(post("/confirm-session").header("sessionToken", sessionToken)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(new ObjectMapper().writeValueAsString(
                                                        confirmSessionInput)))
                                        .andExpect(status().isBadRequest());
                }

                @Test
                void it_shouldConfirmSession() throws JsonProcessingException, Exception {
                        SessionId sessionId = sessionTokenGeneratorUtil.decode(
                                        sessionToken);

                        Session session = sessionRepository.get(sessionId);

                        String sessionCode = session.getConfirmationCode().getCode();

                        ConfirmSessionInput confirmSessionInput = new ConfirmSessionInput();

                        confirmSessionInput.setConfirmationCode(sessionCode);

                        mockMvc.perform(post("/confirm-session").header("sessionToken", sessionToken)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(new ObjectMapper().writeValueAsString(
                                                        confirmSessionInput)))
                                        .andExpect(status().isOk());

                        assertEquals(session.getStatus().getClass(), SessionStatusConfirmated.class);
                }

        }
}

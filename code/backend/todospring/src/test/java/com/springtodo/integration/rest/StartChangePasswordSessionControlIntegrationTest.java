package com.springtodo.integration.rest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSaveSession;
import com.springtodo.core.identity_and_access.domain.exception.CouldNotSaveUser;
import com.springtodo.core.identity_and_access.domain.repository.UserRepository;
import com.springtodo.rest.pojo.start_change_password_session_control.StartChangePasswordSessionRequestBody;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@SpringBootTest(classes = App.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@EnableCaching
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class StartChangePasswordSessionControlIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Value("${jwtSecretKey}")
    private String secretKey;

    private String userEmail = "some@email1.com";
    private String userPassword = "#somePassword";

    @BeforeEach
    void setUp() throws CouldNotSaveUser, CouldNotSaveSession {
        User user = User.create(userEmail, userPassword);

        userRepository.save(user);
    }

    @Test
    void it_shouldReturnMessageUserEmailMustBeInformedWithStatus400() throws JsonProcessingException, Exception {

        ArrayList<String> messages = new ArrayList<String>();

        messages.add("userEmail must be informed");

        StartChangePasswordSessionRequestBody startChangePasswordSessionRequestBody = new StartChangePasswordSessionRequestBody();

        startChangePasswordSessionRequestBody.setUserEmail(null);

        mockMvc.perform(post("/create-change-password-session").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(startChangePasswordSessionRequestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.messages").isArray())
                .andExpect(jsonPath("$.messages").value(messages));
    }

    @Test
    void it_shouldReturnMessageUserEmailMustBeAnEmailWithStatus400() throws JsonProcessingException, Exception {

        ArrayList<String> messages = new ArrayList<String>();

        messages.add("userEmail must be an email");

        StartChangePasswordSessionRequestBody startChangePasswordSessionRequestBody = new StartChangePasswordSessionRequestBody();

        startChangePasswordSessionRequestBody.setUserEmail("some invalid email");

        mockMvc
                .perform(post("/create-change-password-session").contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(startChangePasswordSessionRequestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.messages").isArray())
                .andExpect(jsonPath("$.messages").value(messages));
    }

    @Test
    void it_shouldReturnASessionTokenWithStatus200() throws JsonProcessingException, Exception {
        SecretKey secretKeyBytes = Keys.hmacShaKeyFor(secretKey.getBytes());

        StartChangePasswordSessionRequestBody startChangePasswordSessionRequestBody = new StartChangePasswordSessionRequestBody();

        startChangePasswordSessionRequestBody.setUserEmail(userEmail);

        MvcResult responseResult = mockMvc
                .perform(post("/create-change-password-session").contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(startChangePasswordSessionRequestBody)))
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

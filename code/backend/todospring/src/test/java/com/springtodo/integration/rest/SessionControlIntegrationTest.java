package com.springtodo.integration.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springtodo.App;
import com.springtodo.core.identity_and_access.infrastructure.persistence.jpa.entity.UserJpa;
import com.springtodo.core.identity_and_access.infrastructure.persistence.jpa.repository.UserJpaRepository;
import com.springtodo.rest.pojo.AuthenticationInputJson;

@SpringBootTest(classes = App.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class SessionControlIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Value("")
    private String secretKey;

    private String userName = "Some user";
    private String userEmail = "some@email1.com";
    private String userPassword = "#somePassword";
    private String userFullname = "Some full name";

    @BeforeEach
    void setUp() {
        UserJpa userJpa = new UserJpa();

        userJpa.setName(this.userName);
        userJpa.setEmail(this.userEmail);
        userJpa.setPassword(this.userPassword);
        userJpa.setFullname(this.userFullname);

        userJpaRepository.save(userJpa);
    }

    @Test
    void it_shouldReturnUserNotFoundWithStatus400() throws JsonProcessingException, Exception {
        String aWrongEmail = "wrong.email@email.com";

        mockMvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .writeValueAsString(new AuthenticationInputJson(
                                        aWrongEmail,
                                        "aPassword"))))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.message").value("User not found with email: " + aWrongEmail));
        ;
    }

    void it_shouldReturnInvalidPasswordWithStatus400() {
    }

    void it_shouldReturnASessionTokenWithStatus200() {
    }
}

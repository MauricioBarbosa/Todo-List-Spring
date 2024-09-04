package com.springtodo.integration.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springtodo.App;
import com.springtodo.core.autentication.infrastructure.persistence.repository.hibernate_impl.jpa.model.UserJpa;
import com.springtodo.core.autentication.infrastructure.persistence.repository.hibernate_impl.jpa.repository.UserJpaRepository;
import com.springtodo.rest.pojo.AuthenticationInputJson;

import jakarta.transaction.Transactional;

@SpringBootTest(classes = App.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class AuthenticationControlIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserJpaRepository userJpaRepository;

    private String userName = "Some user";
    private String userEmail = "some@email.com";
    private String userPassword = "#somePassword";
    private String userFullname = "Some full name";

    @BeforeEach()
    void setUp() {
        UserJpa userJpa = new UserJpa();

        userJpa.setId(new Random().nextLong());
        userJpa.setName(this.userName);
        userJpa.setEmail(this.userEmail);
        userJpa.setPassword(this.userPassword);
        userJpa.setFullname(this.userFullname);

        userJpaRepository.save(userJpa);
    }

    @Test
    @Description("It should return user not found exception")
    public void testLoginUserNotFoundError() throws Exception {

        mockMvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .writeValueAsString(new AuthenticationInputJson("wrong.email@email.com",
                                        "aPassword"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Description("It should return invalid password")
    public void testLoginInvalidPasswordError() throws Exception {
        mockMvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper()
                                .writeValueAsString(new AuthenticationInputJson(this.userEmail,
                                        "aWrongPassword"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Description("It should return success with session token")
    public void testLoginSuccess() throws Exception {
    }
}

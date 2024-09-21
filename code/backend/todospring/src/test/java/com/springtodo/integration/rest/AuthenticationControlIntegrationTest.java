package com.springtodo.integration.rest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springtodo.App;
import com.springtodo.core.autentication.infrastructure.persistence.repository.hibernate_impl.jpa.model.UserJpa;
import com.springtodo.core.autentication.infrastructure.persistence.repository.hibernate_impl.jpa.repository.UserJpaRepository;
import com.springtodo.rest.pojo.AuthenticationInputJson;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
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

        @Value("${jwtSecretKey}")
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
        @Description("It should return user not found exception")
        public void testLoginUserNotFoundError() throws Exception {
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

        @Test
        @Description("It should return invalid password")
        public void testLoginInvalidPasswordError() throws Exception {

                String aWrongPassword = "aWrongPassword";

                mockMvc.perform(
                                post("/login")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(new ObjectMapper()
                                                                .writeValueAsString(new AuthenticationInputJson(
                                                                                this.userEmail,
                                                                                aWrongPassword))))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.message").isString())
                                .andExpect(jsonPath("$.message").value("Invalid password"));
        }

        @Test
        @Description("It should return success with session token")
        public void testLoginSuccess() throws Exception {
                SecretKey secretKeyBytes = Keys.hmacShaKeyFor(secretKey.getBytes());

                MvcResult responseResult = mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(
                                                new AuthenticationInputJson(this.userEmail, this.userPassword))))
                                .andExpect(status().isOk())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.token").isString()).andReturn();

                JSONObject responseBody = new JSONObject(responseResult.getResponse().getContentAsString());
                String token = responseBody.getString("token");

                assertDoesNotThrow(() -> {
                        Jwts.parser().verifyWith(secretKeyBytes).build().parseSignedClaims(token).getPayload();
                });
        }
}

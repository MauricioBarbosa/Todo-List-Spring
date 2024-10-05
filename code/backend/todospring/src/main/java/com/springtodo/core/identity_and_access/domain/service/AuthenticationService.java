package com.springtodo.core.autentication.domain.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.springtodo.core.autentication.domain.entity.User;
import com.springtodo.core.autentication.domain.enums.AuthenticationStates;
import com.springtodo.core.autentication.domain.exception.CouldNotCreateSession;
import com.springtodo.core.autentication.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.autentication.domain.exception.InvalidPassword;
import com.springtodo.core.autentication.domain.exception.UserNotFoundException;
import com.springtodo.core.autentication.domain.repository.UserRepository;

@Service
public class AuthenticationService {
    @Autowired
    private SessionTokenGeneratorService sessionGeneratorService;
    @Autowired
    private UserRepository userRepository;

    private Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    @Value("${sessionExpirationInSeconds}")
    private Long sessionExpirationInSeconds;

    public User authenticateUser(String email, String password)
            throws InvalidPassword, UserNotFoundException, CouldNotRetrieveUser {
        Map<String, String> logPayload = new HashMap<>();
        logPayload.put("password", password);
        logPayload.put("email", email);

        log.info("Recovering user", logPayload);

        User user = this.userRepository.getUserByEmailAddress(email);

        Map<String, String> logPayloadAfterUser = new HashMap<>(logPayload);
        logPayloadAfterUser.put("user", user.toString());

        if (!user.isThisUser(password)) {
            log.error("Invalid password", logPayloadAfterUser);

            throw new InvalidPassword("Invalid password");
        }

        return user;
    }

    public String startSession(String email, String password)
            throws CouldNotRetrieveUser, UserNotFoundException, InvalidPassword, CouldNotCreateSession {

        Map<String, String> logPayload = new HashMap<>();
        logPayload.put("password", password);
        logPayload.put("email", email);

        log.info("Recovering user", logPayload);

        User user = this.userRepository.getUserByEmailAddress(email);

        Map<String, String> logPayloadAfterUser = new HashMap<>(logPayload);
        logPayloadAfterUser.put("user", user.toString());

        if (!user.isThisUser(password)) {
            log.error("Invalid password", logPayloadAfterUser);

            throw new InvalidPassword("Invalid password");
        }

        log.info("Generating session", logPayloadAfterUser);

        String sessionToken = this.sessionGeneratorService.createSession(user.getUserId(), user.getEmail(),
                sessionExpirationInSeconds, AuthenticationStates.CONFIRMATION_PENDING);

        System.out.println(user.toString());

        log.info("Returning session", logPayloadAfterUser);

        return sessionToken;
    }
}

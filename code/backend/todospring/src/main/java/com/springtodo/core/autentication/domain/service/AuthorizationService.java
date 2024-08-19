package com.springtodo.core.autentication.domain.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.springtodo.core.autentication.domain.entity.User;
import com.springtodo.core.autentication.domain.exception.CouldNotCreateSession;
import com.springtodo.core.autentication.domain.exception.CouldNotRetrieveUser;
import com.springtodo.core.autentication.domain.exception.InvalidPassword;
import com.springtodo.core.autentication.domain.exception.UserNotFoundException;
import com.springtodo.core.autentication.domain.repository.UserRepository;

@Service
public class AuthorizationService {
    @Autowired
    private SessionGeneratorService sessionGeneratorService;
    @Autowired
    private UserRepository userRepository;

    private Logger LOG = LoggerFactory.getLogger(AuthorizationService.class);

    @Value("${sessionExpirationInSeconds}")
    private Long sessionExpirationInSeconds;

    public String startSession(String email, String password)
            throws CouldNotRetrieveUser, UserNotFoundException, InvalidPassword, CouldNotCreateSession {

        Map<String, String> logPayload = new HashMap<>();
        logPayload.put("password", password);
        logPayload.put("email", email);

        LOG.info("Recovering user", logPayload);

        User user = this.userRepository.getUserByEmailAddress(email);

        Map<String, String> logPayloadAfterUser = new HashMap<>(logPayload);
        logPayloadAfterUser.put("user", user.toString());

        if (!user.isThisUser(password)) {
            LOG.error("Invalid password", logPayloadAfterUser);

            throw new InvalidPassword("Invalid password");
        }

        this.LOG.info("Generating session", logPayloadAfterUser);

        String sessionToken = this.sessionGeneratorService.createSession(user.getId(), user.getEmail(),
                sessionExpirationInSeconds);

        System.out.println(user.toString());

        this.LOG.info("Returning session", logPayloadAfterUser);

        return sessionToken;
    }
}

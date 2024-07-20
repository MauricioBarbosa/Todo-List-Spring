package com.springtodo.core.autentication.infrastructure.service.sessiongenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.springtodo.core.autentication.domain.service.SessionGeneratorService;

public class JjwtSessionGeneratorService extends SessionGeneratorService {

    @Autowired
    private Environment env;

    private String secretKey;
    private String applicationTimeZone;
    private String sessionIssuer;

    public JjwtSessionGeneratorService() {
        super();
        this.secretKey = env.getProperty("jwt.SecretKey");
        this.applicationTimeZone = env.getProperty("app.Timezone");
        this.sessionIssuer = env.getProperty("app.sessionIssuer");
    }

    @Override
    public String createSession(String userId, String email, String expirationInMinutes) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createSession'");
    }

}

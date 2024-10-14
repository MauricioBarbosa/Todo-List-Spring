package com.springtodo.core.identity_and_access.application.usecase;

import com.springtodo.core.identity_and_access.application.dto.StartSessionInput;
import com.springtodo.core.identity_and_access.application.dto.StartSessionOutput;
import com.springtodo.core.identity_and_access.application.utils.SessionTokenGeneratorUtil;
import com.springtodo.core.identity_and_access.domain.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StartSession {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionTokenGeneratorUtil sessionTokenGeneratorUtil;

    public StartSessionOutput execute(StartSessionInput startSessionInput) {
        /*
        * 1 - Should create value_objects aUserPassword and aUserEmail
        * 2 - Should create a session
        * 2.2 - Error on creating jwt token from session (using id)
        * 2.2.2 - throw error
          3 - Returns jwt from token
        */
        throw new RuntimeException("Method not implemented yet");
    }
}

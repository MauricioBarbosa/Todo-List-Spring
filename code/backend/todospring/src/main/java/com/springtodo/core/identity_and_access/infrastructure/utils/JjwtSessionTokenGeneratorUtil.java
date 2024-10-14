package com.springtodo.core.identity_and_access.infrastructure.utils;

import com.springtodo.core.identity_and_access.application.exception.CouldNotGenerateToken;
import com.springtodo.core.identity_and_access.application.utils.SessionTokenGeneratorUtil;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JjwtSessionTokenGeneratorUtil extends SessionTokenGeneratorUtil {
    @Value("${jwtSecretKey}")
    private String secretKey;

    @Value("${applicationTimeZone}")
    private String applicationTimeZone;

    @Value("${jwtSessionIssuer}")
    private String sessionIssuer;

    @Value("${jwtSessionSubject}")
    private String sessionSubject;

	@Override
	public String generate(String sessionId) throws CouldNotGenerateToken {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'generate'");
	}
}

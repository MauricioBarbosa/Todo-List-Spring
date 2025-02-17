package com.springtodo.config;

import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.springtodo.core.identity_and_access.application.utils.SessionTokenGeneratorUtil;
import com.springtodo.core.identity_and_access.domain.provider.EmailProvider;
import com.springtodo.core.identity_and_access.domain.repository.SessionRepository;
import com.springtodo.core.identity_and_access.domain.repository.UserRepository;
import com.springtodo.core.identity_and_access.infrastructure.mock.EmailProviderMock;
import com.springtodo.core.identity_and_access.infrastructure.persistence.repository.hibernate.HibernateUserRepository;
import com.springtodo.core.identity_and_access.infrastructure.persistence.repository.in_memory.InMemorySessionRepository;
import com.springtodo.core.identity_and_access.infrastructure.utils.JjwtSessionTokenGeneratorUtil;

@Configuration
public class AppConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }

    @Bean
    ConcurrentMapCacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        return cacheManager;
    }

    @Bean
    public UserRepository userRepository() {
        return new HibernateUserRepository();
    }

    @Bean
    public SessionRepository sessionRepository() {
        return new InMemorySessionRepository();
    }

    @Bean
    public EmailProvider emailSenderProvider() {
        return new EmailProviderMock();
    }

    @Bean
    public SessionTokenGeneratorUtil sessionTokenGeneratorUtil() {
        return new JjwtSessionTokenGeneratorUtil();
    }
}

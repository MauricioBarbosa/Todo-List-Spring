package com.springtodo.config;

import com.springtodo.core.identity_and_access.domain.repository.SessionRepository;
import com.springtodo.core.identity_and_access.domain.repository.UserRepository;
import com.springtodo.core.identity_and_access.infrastructure.persistence.repository.hibernate.HibernateUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.CharacterEncodingFilter;

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
    public UserRepository userRepository() {
        return new HibernateUserRepository();
    }
}

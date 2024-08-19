package com.springtodo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.springtodo.core.autentication.domain.repository.UserRepository;
import com.springtodo.core.autentication.infrastructure.persistence.repository.hibernate_impl.HibernateUserRepository;

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
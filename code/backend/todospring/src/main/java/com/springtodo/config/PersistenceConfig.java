package com.springtodo.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class PersistenceConfig {

    @Autowired
    private Environment env;

    private static final Logger LOG = LoggerFactory.getLogger(PersistenceConfig.class);

    @Bean
    public DataSource dataSource() {

        LOG.info("Starting persistence", env.toString());

        DriverManagerDataSource dataSource = new DriverManagerDataSource(env.getProperty("url"),
                env.getProperty("user"), env.getProperty("password"));
        dataSource.setDriverClassName(env.getProperty("driverClassName"));
        return dataSource;
    }
}

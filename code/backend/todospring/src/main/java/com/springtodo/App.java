package com.springtodo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
@EnableCaching
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
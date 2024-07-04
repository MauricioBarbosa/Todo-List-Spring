package com.springtodo.rest.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springtodo.rest.model.AuthenticationInputJson;

@RestController
public class Autentication {

    @PostMapping("/login")
    public String login(@RequestBody AuthenticationInputJson authenticationInputJson) {
        System.out.println(authenticationInputJson.getEmail());
        return "My application is running";
    }
}

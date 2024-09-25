package com.springtodo.core.autentication.domain.service;

public abstract class EmailSenderService {
    public abstract void sendToken(String token);
}

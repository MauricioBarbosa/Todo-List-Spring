package com.springtodo.core.identity_and_access.application.usecase;

import org.springframework.stereotype.Service;

import com.springtodo.core.identity_and_access.application.dto.IsSessionConfirmatedInput;

@Service
public class IsSessionConfirmed {

    public boolean execute(
        IsSessionConfirmatedInput isSessionConfirmatedInput
    ) {
        throw new RuntimeException("not implemented yet");
    }
}

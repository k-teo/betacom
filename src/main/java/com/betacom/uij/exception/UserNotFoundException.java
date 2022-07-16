package com.betacom.uij.exception;

import com.betacom.uij.dto.response.MessageResponseType;

public class UserNotFoundException extends Exception {

    public UserNotFoundException() {
        this(MessageResponseType.USER_NOT_FOUND.getMessage());
    }

    public UserNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}

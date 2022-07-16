package com.betacom.uij.exception;

import com.betacom.uij.dto.response.MessageResponseType;

public class NotUniqueLoginException extends Exception{

    public NotUniqueLoginException() {
        this(MessageResponseType.USER_NOT_FOUND.getMessage());
    }

    public NotUniqueLoginException(String errorMessage) {
        super(errorMessage);
    }

}

package com.betacom.uij.dto.response;

public enum MessageResponseType {

    SUCCESSFUL_REGISTRATION("Registering successfull."),
    ITEM_CREATED("Item created successfull."),
    TOKEN_ERROR("You have not provided an authentication token, the one provided has expired, was revoked or is not authentic."),
    NOT_UNIQUE_LOGIN("Login is already taken!"),
    USER_NOT_FOUND("User was not found.");

    private final String message;

    MessageResponseType(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}

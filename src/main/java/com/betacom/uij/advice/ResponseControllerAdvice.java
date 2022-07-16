package com.betacom.uij.advice;

import com.betacom.uij.exception.NotUniqueLoginException;
import com.betacom.uij.exception.UserNotFoundException;
import com.betacom.uij.dto.response.MessageResponseDto;
import com.betacom.uij.dto.response.MessageResponseType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ResponseControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<MessageResponseDto> handleUserNotFoundException() {
        return new ResponseEntity<>(new MessageResponseDto(MessageResponseType.USER_NOT_FOUND.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotUniqueLoginException.class)
    public ResponseEntity<MessageResponseDto> handleNotUniqueLoginException() {
        return new ResponseEntity<>(new MessageResponseDto(MessageResponseType.NOT_UNIQUE_LOGIN.getMessage()), HttpStatus.BAD_REQUEST);
    }
}

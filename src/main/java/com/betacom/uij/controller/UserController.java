package com.betacom.uij.controller;

import com.betacom.uij.exception.NotUniqueLoginException;
import com.betacom.uij.dto.request.LoginRequestDto;
import com.betacom.uij.dto.request.RegisterRequestDto;
import com.betacom.uij.dto.response.MessageResponseDto;
import com.betacom.uij.dto.response.TokenResponseDto;
import com.betacom.uij.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@AllArgsConstructor
public class UserController {

    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return userService.login(loginRequestDto);
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponseDto> register(@RequestBody RegisterRequestDto registerRequestDto) throws NotUniqueLoginException {
        return userService.register(registerRequestDto);
    }
}

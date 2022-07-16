package com.betacom.uij.security;

import com.betacom.uij.dto.response.MessageResponseDto;
import com.betacom.uij.dto.response.MessageResponseType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                         AuthenticationException authException) throws IOException {

        MessageResponseDto messageResponseDto = new MessageResponseDto(MessageResponseType.TOKEN_ERROR.getMessage());
        String json = new ObjectMapper().writeValueAsString(messageResponseDto);

        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.getWriter().write(json);
        httpServletResponse.flushBuffer();
    }
}

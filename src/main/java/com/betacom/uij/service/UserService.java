package com.betacom.uij.service;

import com.betacom.uij.exception.NotUniqueLoginException;
import com.betacom.uij.dto.request.LoginRequestDto;
import com.betacom.uij.dto.request.RegisterRequestDto;
import com.betacom.uij.dto.response.MessageResponseDto;
import com.betacom.uij.dto.response.MessageResponseType;
import com.betacom.uij.dto.response.TokenResponseDto;
import com.betacom.uij.model.User;
import com.betacom.uij.repository.UserRepository;
import com.betacom.uij.security.JwtUtilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtilities jwtUtilities;
    private final UserDetailService userDetailService;

    public ResponseEntity<TokenResponseDto> login(LoginRequestDto loginRequestDto) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getLogin(), loginRequestDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseEntity<>(new TokenResponseDto(jwtUtilities.generateToken(userDetailService.loadUserByUsername(loginRequestDto.getLogin()))), HttpStatus.OK);
    }

    public ResponseEntity<MessageResponseDto> register(RegisterRequestDto registerRequestDto) throws NotUniqueLoginException {
        if (Boolean.TRUE.equals(userRepository.existsByLogin(registerRequestDto.getLogin()))) {
            throw new NotUniqueLoginException();
        }

        User user = new User(null, registerRequestDto.getLogin(), encoder.encode(registerRequestDto.getPassword()), new HashSet<>());
        userRepository.save(user);

        return new ResponseEntity<>(new MessageResponseDto(MessageResponseType.SUCCESSFUL_REGISTRATION.getMessage()), HttpStatus.OK);
    }
}

package com.betacom.uij.service;

import com.betacom.uij.dto.request.LoginRequestDto;
import com.betacom.uij.dto.request.RegisterRequestDto;
import com.betacom.uij.dto.response.MessageResponseDto;
import com.betacom.uij.dto.response.MessageResponseType;
import com.betacom.uij.dto.response.TokenResponseDto;

import com.betacom.uij.exception.NotUniqueLoginException;
import com.betacom.uij.model.User;
import com.betacom.uij.repository.UserRepository;
import com.betacom.uij.security.JwtUtilities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder encoder;
    @Mock
    JwtUtilities jwtUtilities;
    @Mock
    UserDetailService userDetailService;

    @InjectMocks
    UserService userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    void testLogin() {
        //given
        String token = "generateTokenResponse";
        when(jwtUtilities.generateToken(any())).thenReturn(token);
        when(userDetailService.loadUserByUsername(anyString())).thenReturn(buildUserDetails());

        //when
        ResponseEntity<TokenResponseDto> result = userService.login(new LoginRequestDto("login", "password"));

        //then
        assertThat(result, is(notNullValue()));
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        assertThat(result.getBody(), is(notNullValue()));
        assertThat(result.getBody().getToken(), is(token));

    }

    @Test
    void testRegister() throws NotUniqueLoginException {
        //given
        String login = "login";
        String password = "password";
        String encodedPassword = "encoded";
        RegisterRequestDto registerRequestDto = buildRegisterRequest(login, password);
        when(userRepository.existsByLogin(anyString())).thenReturn(Boolean.FALSE);
        when(encoder.encode(any())).thenReturn(encodedPassword);

        //when
        ResponseEntity<MessageResponseDto> result = userService.register(registerRequestDto);

        //then
        assertThat(result, is(notNullValue()));
        assertThat(result.getStatusCode(), is(HttpStatus.OK));
        assertThat(result.getBody(), is(notNullValue()));
        assertThat(result.getBody().getDescription(), is(MessageResponseType.SUCCESSFUL_REGISTRATION.getMessage()));
        verify(userRepository, times(1)).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getUuid(), is(nullValue()));
        assertThat(savedUser.getLogin(), is(login));
        assertThat(savedUser.getPassword(), is(encodedPassword));
        assertThat(savedUser.getItems(), hasSize(0));

    }

    @Test
    void testRegisterShouldThrowNotUniqueLoginException() {
        //given
        when(userRepository.existsByLogin(anyString())).thenReturn(Boolean.TRUE);

        //when
        Throwable thrown = assertThrows(NotUniqueLoginException.class,
                () -> userService.register(new RegisterRequestDto("login", "password")));

        //then
        assertThat(thrown, isA(NotUniqueLoginException.class));
        assertThat(thrown.getMessage(), is(MessageResponseType.USER_NOT_FOUND.getMessage()));
        verify(userRepository, times(1)).existsByLogin(any());
        verifyNoMoreInteractions(userRepository);
    }

    private RegisterRequestDto buildRegisterRequest(String login, String password) {
        return new RegisterRequestDto(login, password);
    }

    private UserDetails buildUserDetails() {
        return new org.springframework.security.core.userdetails.User("test", "test", new ArrayList<>());
    }

}

package com.betacom.uij.service;

import com.betacom.uij.dto.response.MessageResponseType;
import com.betacom.uij.exception.UserNotFoundException;
import com.betacom.uij.model.User;
import com.betacom.uij.repository.UserRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import org.springframework.security.core.userdetails.UserDetails;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserDetailServiceUnitTest {

    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserDetailService userDetailService;

    @Test
    void testLoadUserByUsername() {
        //given
        String username = "username";
        User user = buildUser(username);
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(user));
        //when
        UserDetails result = userDetailService.loadUserByUsername(username);

        //then
        assertThat(result, is(notNullValue()));
        assertThat(result.getUsername(), is(user.getLogin()));
        assertThat(result.getPassword(), is(user.getPassword()));
        assertThat(result.getAuthorities(), hasSize(0));
        verify(userRepository, times(1)).findByLogin(any());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testLoadUserByUsernameShouldThrowUserNotFoundException() {
        //given
        String username = "username";
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.empty());
        //when
        Throwable thrown = assertThrows(UserNotFoundException.class, () -> userDetailService.loadUserByUsername(username));

        //then
        assertThat(thrown, isA(UserNotFoundException.class));
        assertThat(thrown.getMessage(), is(MessageResponseType.USER_NOT_FOUND.getMessage()));
        verify(userRepository, times(1)).findByLogin(any());
        verifyNoMoreInteractions(userRepository);

    }

    private User buildUser(String userName) {
        User user = new User();
        user.setLogin(userName);
        user.setPassword("password");
        user.setUuid(UUID.randomUUID());
        return user;
    }
}

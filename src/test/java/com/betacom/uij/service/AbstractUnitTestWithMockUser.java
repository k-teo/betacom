package com.betacom.uij.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public abstract class AbstractUnitTestWithMockUser {

    protected void mockSecurity() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        User user = new User("username", "password", List.of());
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user, "password");
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(authRequest.getPrincipal());
    }
}

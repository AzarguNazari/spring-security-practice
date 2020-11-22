package com.example.example6.service;

import com.example.example6.security.CustomAuthentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Value("${key}")
    private String key;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(authentication.getName().equals(key)){
            return new CustomAuthentication(null, null, null);
        }
        else{
            throw new BadCredentialsException("Sorry, the user is not authenticated");
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return CustomAuthentication.class == aClass;
    }
}

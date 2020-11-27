package com.example.demo.security.providers;

import com.example.demo.security.authentications.TokenAuthentication;
import com.example.demo.security.managers.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * An AuthenticationProvider implementation takes care of verifying an authentication request.
 *
 * TokenAuthProvider Provider takes care of verifying token authentication
 */


@Component
public class TokenAuthProvider implements AuthenticationProvider {

    @Autowired
    private TokenManager tokenManager;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = authentication.getName();
        boolean exists = tokenManager.contains(token);

        if (exists) {
            return new TokenAuthentication(token, null, null);
        }

        throw new BadCredentialsException(":(");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return TokenAuthentication.class.equals(aClass);
    }
}


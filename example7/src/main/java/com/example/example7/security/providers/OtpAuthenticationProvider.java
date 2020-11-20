package com.example.example7.security.providers;


import com.example.example7.entities.Otp;
import com.example.example7.repositories.OtpRepository;
import com.example.example7.security.authentications.OtpAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class OtpAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private OtpRepository otpRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String otp = (String) authentication.getCredentials();

        Optional<Otp> o = otpRepository.findOtpByUsername(username);

        if (o.isPresent()) {
            return new OtpAuthentication(username, otp, Arrays.asList(() -> "read"));
        }

        throw new BadCredentialsException(":(");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return OtpAuthentication.class.equals(aClass);
    }
}
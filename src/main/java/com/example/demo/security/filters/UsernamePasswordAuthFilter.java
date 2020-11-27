package com.example.demo.security.filters;

import com.example.demo.entities.Otp;
import com.example.demo.repositories.OtpRepository;
import com.example.demo.security.authentications.OtpAuthentication;
import com.example.demo.security.authentications.UsernamePasswordAuthentication;
import com.example.demo.security.managers.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

/**
 *
 * This filter class takes care of filter each request based on basic authentication and otp
 * If a user is on his/her first visit, he/she will enter by basic authentication (username and password)
 * After successfully authentication, there will be stored the token on the database for that specific user and for each next time visit, user can use that token
 *
 */

public class UsernamePasswordAuthFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private TokenManager tokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {

        // Step 1: username & password
        // Step 2: username & otp
        var username = request.getHeader("username");
        var password = request.getHeader("password");
        var otp = request.getHeader("otp");

        if (otp == null) {
            // step 1
            Authentication a = new UsernamePasswordAuthentication(username, password);
            a = authenticationManager.authenticate(a);
            // we generate an OTP
            String code = String.valueOf(new Random().nextInt(9999) + 1000);

            Otp otpEntity = new Otp();
            otpEntity.setUsername(username);
            otpEntity.setOtp(code);
            otpRepository.save(otpEntity);
        } else {
            // step 2
            Authentication a = new OtpAuthentication(username, otp);
            a = authenticationManager.authenticate(a);
            // we issue a token
            var token = UUID.randomUUID().toString();
            tokenManager.add(token);
            response.setHeader("Authorization", token);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath().equals("/login");
    }
}
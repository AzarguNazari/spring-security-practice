package com.example.example7.controllers;

import com.example.example7.entities.Otp;
import com.example.example7.entities.User;
import com.example.example7.repositories.OtpRepository;
import com.example.example7.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    @GetMapping("/hello")
    public String hello() {
        return "Hello!";
    }

    @GetMapping("/users/basic")
    public List<User> getBasicUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/users/otp")
    public List<Otp> getOtpUsers(){
        return otpRepository.findAll();
    }
}
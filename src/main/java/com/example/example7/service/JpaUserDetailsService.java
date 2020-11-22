package com.example.example7.service;

import com.example.example7.entities.User;
import com.example.example7.repositories.UserRepository;
import com.example.example7.security.model.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> o = userRepository.findUserByUsername(username);
        User u = o.orElseThrow(() -> new UsernameNotFoundException(":("));
        return new SecurityUser(u);
    }
}
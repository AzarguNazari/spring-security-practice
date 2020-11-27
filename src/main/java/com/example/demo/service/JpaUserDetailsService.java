package com.example.demo.service;

import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.model.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserDetailsService is a core interface which loads user-specific data.
 * It is used throughout the framework as a user DAO and is the strategy used by the DaoAuthenticationProvider.
 *
 * The interface requires only one read-only method, which simplifies support for new data-access strategies.
 *
 * JpaUserDetailsService is a DetailsService class that loads data from the databases
 *
 */


@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        var o = userRepository.findUserByUsername(username);
        User u = o.orElseThrow(() -> new UsernameNotFoundException(":("));
        return new SecurityUser(u);
    }
}

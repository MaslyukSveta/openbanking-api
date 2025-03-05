package com.example.openbanking.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;


/**
 * Implementation of {@link UserDetailsService} for user authentication.
 * This service loads user details based on the username.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * Loads the user details by the provided username.
     *
     * @param username The username to look up.
     * @return The {@link UserDetails} object representing the authenticated user.
     * @throws UsernameNotFoundException If the username is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        if (!"user@example.com".equals(username)) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return new User(username, "", Collections.emptyList());
    }
}
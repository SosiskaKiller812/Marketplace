package com.marketplace.auth.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.marketplace.auth.entities.User;
import com.marketplace.auth.entities.UserDetailsImpl;
import com.marketplace.auth.exceptions.UserNotFoundException;
import com.marketplace.auth.repositories.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username).orElseThrow(
                () -> new UserNotFoundException(username));

        return new UserDetailsImpl(user);
    }

}

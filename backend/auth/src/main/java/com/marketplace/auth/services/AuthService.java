package com.marketplace.auth.services;

import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.marketplace.auth.entities.Role;
import com.marketplace.auth.entities.Token;
import com.marketplace.auth.entities.User;
import com.marketplace.auth.entities.UserDetailsImpl;
import com.marketplace.auth.entities.Request.LoginRequest;
import com.marketplace.auth.entities.Request.RegisterRequest;
import com.marketplace.auth.entities.Response.AuthenticationResponse;
import com.marketplace.auth.entities.Response.UserResponse;
import com.marketplace.auth.mappers.UserMapper;
import com.marketplace.auth.repositories.RoleRepository;
import com.marketplace.auth.repositories.TokenRepository;
import com.marketplace.auth.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AuthService {

    private final TokenRepository tokenRepository;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    public UserResponse register(RegisterRequest registerRequest) {

        Role role = roleRepository.findByName("USER").orElseThrow();

        User newUser = new User(
                registerRequest.getName(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()),
                Set.of(role));

        userRepository.save(newUser);

        return userMapper.toUserResponse(newUser);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getName(), loginRequest.getPassword()));

        User user = userRepository.findByName(loginRequest.getName()).orElseThrow();

        UserDetails userDetails = new UserDetailsImpl(user);

        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        Token token = new Token(refreshToken, user);
        tokenRepository.save(token);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    @Transactional
    public AuthenticationResponse refreshToken(String incomingRefreshToken) {

        String username = jwtService.extractUsername(incomingRefreshToken);

        User user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found"));

        Token existingToken = tokenRepository.findByRefreshToken(incomingRefreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found in database"));

        if (!existingToken.getUser().equals(user)) {
            throw new RuntimeException("Something went wrong with refreshToken and user");
        }

        UserDetails userDetails = new UserDetailsImpl(user);

        if (jwtService.isRefreshTokenValid(incomingRefreshToken, userDetails)) {

            String newAccessToken = jwtService.generateAccessToken(userDetails);
            String newRefreshToken = jwtService.generateRefreshToken(userDetails);

            Token newEntityRefreshToken = new Token(newRefreshToken, user);

            tokenRepository.deleteByRefreshToken(incomingRefreshToken);
            tokenRepository.save(newEntityRefreshToken);

            return new AuthenticationResponse(newAccessToken, newRefreshToken);
        } else {
            throw new RuntimeException("Refresh token not valid");
        }

    }

    @Transactional
    public void addRole(String username, String roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Transactional
    public void removeRole(String username, String roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.getRoles().add(role);
        userRepository.save(user);
    }
}

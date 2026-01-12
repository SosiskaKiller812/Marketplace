package com.marketplace.auth.services;

import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.marketplace.auth.repositories.TokenRepository;
import com.marketplace.auth.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AuthService {

    private final TokenRepository tokenRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    public UserResponse register(RegisterRequest registerRequest) {
        User newUser = new User(
                registerRequest.getName(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()),
                Set.of(new Role("USER")));

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

        Token token = new Token(refreshToken, false, user);
        tokenRepository.save(token);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request,
            HttpServletResponse response) {

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authorizationHeader.substring(7);
        String username = jwtService.extractUsername(token);

        User user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found"));

        UserDetails userDetails = new UserDetailsImpl(user);

        if (jwtService.isRefreshTokenValid(token, userDetails)) {

            String accessToken = jwtService.generateAccessToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);

            Token newRefreshToken = new Token(refreshToken, false, user);
            tokenRepository.save(newRefreshToken);

            return new ResponseEntity<>(new AuthenticationResponse(accessToken, refreshToken), HttpStatus.OK);

        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}

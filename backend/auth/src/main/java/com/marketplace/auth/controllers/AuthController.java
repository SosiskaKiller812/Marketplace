package com.marketplace.auth.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marketplace.auth.repositories.UserRepository;
import com.marketplace.auth.services.AuthService;
import com.marketplace.auth.entities.Request.LoginRequest;
import com.marketplace.auth.entities.Request.RegisterRequest;
import com.marketplace.auth.entities.Response.AuthenticationResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@AllArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {
        private final UserRepository userRepository;
        private final AuthService authService;

        @PostMapping("/signup")
        public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
                if (userRepository.existsByUsername(registerRequest.getName())) {
                        return ResponseEntity.badRequest().body("Имя пользователя уже занято");
                }

                if (userRepository.existsByEmail(registerRequest.getEmail())) {
                        return ResponseEntity.badRequest().body("Email уже занят");
                }

                authService.register(registerRequest);

                return ResponseEntity.ok("Регистрация прошла успешно");
        }

        @PostMapping("/signin")
        public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
                return ResponseEntity.ok(authService.login(loginRequest));
        }

        @PostMapping("/refresh_token")
        public ResponseEntity<AuthenticationResponse> refreshToken(
                        HttpServletRequest request,
                        HttpServletResponse response) {

                return authService.refreshToken(request, response);
        }

        @PostMapping("/signout")
        public ResponseEntity<?> logoutUser() {
                return ResponseEntity.status(501).body("NEED TO DO");
        }

}

package com.aftermidnight.service.impl;

import com.aftermidnight.dto.AuthResponse;
import com.aftermidnight.dto.LoginRequest;
import com.aftermidnight.dto.RegisterRequest;
import com.aftermidnight.entity.User;
import com.aftermidnight.repository.UserRepository;
import com.aftermidnight.security.JwtUtil;
import com.aftermidnight.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed: Email {} already exists", request.getEmail());
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);
        log.info("User registered successfully with email: {}", request.getEmail());

        String token = jwtUtil.generateToken(user.getUsername());

        return AuthResponse.builder()
                .message("User registered successfully")
                .token(token)
                .build();
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed: User with email {} not found", request.getEmail());
                    return new RuntimeException("Invalid email or password");
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Login failed: Password mismatch for email {}", request.getEmail());
            throw new RuntimeException("Invalid email or password");
        }

        log.info("User logged in successfully with email: {}", request.getEmail());
        String token = jwtUtil.generateToken(user.getUsername());

        return AuthResponse.builder()
                .message("User logged in successfully")
                .token(token)
                .build();
    }
}

package com.aftermidnight.service;

import com.aftermidnight.dto.AuthResponse;
import com.aftermidnight.dto.LoginRequest;
import com.aftermidnight.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}

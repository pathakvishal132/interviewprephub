package com.interviewprephub.backend.service;

import com.interviewprephub.backend.dto.auth.AuthResponse;
import com.interviewprephub.backend.dto.auth.LoginRequest;
import com.interviewprephub.backend.dto.auth.SignupRequest;

public interface UserService {
    AuthResponse login(LoginRequest request);
    AuthResponse signup(SignupRequest request);
    boolean verifyEmail(String token);
}

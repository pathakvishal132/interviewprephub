package com.interviewprephub.backend.controller;

import com.interviewprephub.backend.dto.auth.AuthResponse;
import com.interviewprephub.backend.dto.auth.LoginRequest;
import com.interviewprephub.backend.dto.auth.SignupRequest;
import com.interviewprephub.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = userService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("detail", "Invalid email or password"));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        try {
            AuthResponse response = userService.signup(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("detail", e.getMessage()));
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam(value = "token") String token) {
        try {
            boolean verified = userService.verifyEmail(token);
            if (verified) {
                return ResponseEntity.ok(Map.of("message", "Email verified successfully! You can now login."));
            } else {
                return ResponseEntity.badRequest().body(Map.of("detail", "Invalid or expired verification token"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("detail", e.getMessage()));
        }
    }
}

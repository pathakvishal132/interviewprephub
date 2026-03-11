package com.interviewprephub.backend.serviceImpl;

import com.interviewprephub.backend.dto.auth.AuthResponse;
import com.interviewprephub.backend.dto.auth.LoginRequest;
import com.interviewprephub.backend.dto.auth.SignupRequest;
import com.interviewprephub.backend.entity.User;
import com.interviewprephub.backend.repository.UserRepository;
import com.interviewprephub.backend.service.EmailService;
import com.interviewprephub.backend.service.UserService;
import com.interviewprephub.backend.util.CustomUserDetailsService;
import com.interviewprephub.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getIsVerified()) {
            throw new RuntimeException("Please verify your email first. Check your inbox for verification link.");
        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        String verificationToken = UUID.randomUUID().toString();

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(User.Role.USER)
                .isActive(true)
                .isVerified(false)
                .verificationToken(verificationToken)
                .build();

        userRepository.save(user);

        emailService.sendVerificationEmail(request.getEmail(), verificationToken);

        return AuthResponse.builder()
                .message("Verification email sent. Please check your inbox.")
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public boolean verifyEmail(String token) {
        User user = userRepository.findAll().stream()
                .filter(u -> token.equals(u.getVerificationToken()))
                .findFirst()
                .orElse(null);

        if (user == null) {
            return false;
        }

        user.setIsVerified(true);
        user.setVerificationToken(null);
        userRepository.save(user);

        emailService.sendWelcomeEmail(user.getEmail(), user.getFullName());

        return true;
    }
}

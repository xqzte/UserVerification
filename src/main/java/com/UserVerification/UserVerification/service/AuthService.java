package com.UserVerification.UserVerification.service;

import com.UserVerification.UserVerification.dto.LoginRequest;
import com.UserVerification.UserVerification.dto.LoginResponse;
import com.UserVerification.UserVerification.dto.RegistrationRequest;
import com.UserVerification.UserVerification.dto.RegistrationResponse;
import com.UserVerification.UserVerification.entity.User;
import com.UserVerification.UserVerification.enums.Role;
import com.UserVerification.UserVerification.enums.VerificationStatus;
import com.UserVerification.UserVerification.repository.UserRepository;
import com.UserVerification.UserVerification.security.JwtUtil;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder encoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public RegistrationResponse register(RegistrationRequest request) {

        // block admin self registration
        if (request.getRole() == Role.ADMIN) {
            throw new IllegalArgumentException(
                    "Admin accounts cannot be self registered");
        }

        // block if someone tries to register with the admin email
        if (userRepository.findByEmail(request.getEmail())
                .filter(u -> u.getRole() == Role.ADMIN)
                .isPresent()) {
            throw new IllegalArgumentException(
                    "This email is not available");
        }

        // block duplicate emails
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException(
                    "Email is already registered");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setVerificationStatus(VerificationStatus.PENDING);

        User savedUser = userRepository.save(user);

        return new RegistrationResponse(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getVerificationStatus()
        );
    }
    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return new LoginResponse(token);
    }
}
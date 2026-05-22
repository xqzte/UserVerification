package com.UserVerification.UserVerification.controller;

import com.UserVerification.UserVerification.dto.LoginRequest;
import com.UserVerification.UserVerification.dto.LoginResponse;
import com.UserVerification.UserVerification.dto.RegistrationRequest;
import com.UserVerification.UserVerification.entity.User;
import com.UserVerification.UserVerification.enums.VerificationStatus;
import com.UserVerification.UserVerification.service.AuthService;
import com.UserVerification.UserVerification.service.VerificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final VerificationService verificationService;

    public AuthController(AuthService authService,
                          VerificationService verificationService) {
        this.authService = authService;
        this.verificationService = verificationService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(
            @Valid @RequestBody RegistrationRequest request) {
        User user = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{email}/status")
    public ResponseEntity<Map<String, String>> checkStatus(
            @PathVariable String email) {
        VerificationStatus status = verificationService
                .checkVerificationStatus(email);
        return ResponseEntity.ok(Map.of(
                "email", email,
                "status", status.name()
        ));
    }
}
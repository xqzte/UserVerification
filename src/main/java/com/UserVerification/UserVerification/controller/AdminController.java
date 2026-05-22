package com.UserVerification.UserVerification.controller;

import com.UserVerification.UserVerification.entity.User;
import com.UserVerification.UserVerification.enums.VerificationStatus;
import com.UserVerification.UserVerification.service.VerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final VerificationService verificationService;

    public AdminController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(verificationService.getAllUsers());
    }

    @GetMapping("/users/pending")
    public ResponseEntity<List<User>> getPendingUsers() {
        return ResponseEntity.ok(verificationService.getPendingUsers());
    }

    @PatchMapping("/users/{email}/status")
    public ResponseEntity<User> updateStatus(
            @PathVariable String email,
            @RequestBody Map<String, String> body) {
        VerificationStatus newStatus = VerificationStatus
                .valueOf(body.get("status").toUpperCase());
        User updated = verificationService
                .updateVerificationStatus(email, newStatus);
        return ResponseEntity.ok(updated);
    }
}
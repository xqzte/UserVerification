package com.UserVerification.UserVerification.controller;

import com.UserVerification.UserVerification.entity.User;
import com.UserVerification.UserVerification.enums.VerificationStatus;
import com.UserVerification.UserVerification.service.VerificationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.Map;

@RestController
public class VerificationController {

    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    // POST /verify
    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verify(
            @RequestBody Map<String, String> body) {

        String userId = body.get("userId");
        String category = body.get("category");

        if (userId == null || userId.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", 400,
                    "message", "userId is required"
            ));
        }

        if (category == null || category.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", 400,
                    "message", "category is required"
            ));
        }

        User user = verificationService.getUserById(Long.parseLong(userId));

        return ResponseEntity.ok(Map.of(
                "userId", user.getId(),
                "category", category,
                "status", user.getVerificationStatus().name(),
                "email", user.getEmail(),
                "fullName", user.getFullName()
        ));
    }

    // GET /status/:userId
    @GetMapping("/status/{userId}")
    public ResponseEntity<Map<String, Object>> getStatus(
            @PathVariable Long userId) {
        User user = verificationService.getUserById(userId);
        return ResponseEntity.ok(Map.of(
                "userId", user.getId(),
                "email", user.getEmail(),
                "fullName", user.getFullName(),
                "status", user.getVerificationStatus().name()
        ));
    }


    // PATCH /status/:userId
    @PatchMapping("/status/{userId}")
    public ResponseEntity<Map<String, Object>> updateStatus(
            @PathVariable Long userId,
            @RequestBody Map<String, String> body) {

        String status = body.get("status");

        if (status == null || status.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", 400,
                    "message", "status is required"
            ));
        }

        VerificationStatus newStatus;
        try {
            newStatus = VerificationStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", 400,
                    "message", "Invalid status value. Must be PENDING, APPROVED, REJECTED or CANCELLED"
            ));
        }

        User updated = verificationService.updateVerificationStatus(userId, newStatus);

        return ResponseEntity.ok(Map.of(
                "userId", updated.getId(),
                "email", updated.getEmail(),
                "fullName", updated.getFullName(),
                "status", updated.getVerificationStatus().name()
        ));
    }



}
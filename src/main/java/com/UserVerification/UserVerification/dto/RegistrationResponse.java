package com.UserVerification.UserVerification.dto;

import com.UserVerification.UserVerification.enums.Role;
import com.UserVerification.UserVerification.enums.VerificationStatus;

public class RegistrationResponse {

    private Long id;
    private String fullName;
    private String email;
    private Role role;
    private VerificationStatus verificationStatus;

    public RegistrationResponse(Long id, String fullName, String email,
                                Role role, VerificationStatus verificationStatus) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.verificationStatus = verificationStatus;
    }

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
    public VerificationStatus getVerificationStatus() { return verificationStatus; }
}
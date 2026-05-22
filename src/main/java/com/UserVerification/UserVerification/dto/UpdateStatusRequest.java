package com.UserVerification.UserVerification.dto;

import com.UserVerification.UserVerification.enums.VerificationStatus;

public class UpdateStatusRequest {

    private String name;
    private String email;
    private VerificationStatus newStatus;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNewStatus(VerificationStatus newStatus){
        this.newStatus = newStatus;
    }

    public VerificationStatus getNewStatus(){
        return newStatus;
    }
}

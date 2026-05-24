package com.UserVerification.UserVerification.service;

import com.UserVerification.UserVerification.entity.User;
import com.UserVerification.UserVerification.enums.VerificationStatus;
import com.UserVerification.UserVerification.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VerificationService {

    private UserRepository userRepository;
    private User user;

    public VerificationService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.user = user;
    }

    //check the users verification
    @Transactional
    public VerificationStatus checkVerificationStatus(String email) {

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return VerificationStatus.NON_EXISTENT;
        }

        return user.get().getVerificationStatus();
    }


    // Get all users who are currently PENDING
    @Transactional
    public List<User> getPendingUsers() {
        return userRepository.findAllByVerificationStatus(VerificationStatus.PENDING);
    }

    // Get all users regardless of status
    @Transactional
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //update the users verification status from PENDING to any other one.
    @Transactional
    public User updateVerificationStatus(Long id, VerificationStatus newStatus) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        VerificationStatus currentStatus = user.getVerificationStatus();

        switch (currentStatus) {
            case PENDING:
                if (newStatus == VerificationStatus.APPROVED ||
                        newStatus == VerificationStatus.REJECTED ||
                        newStatus == VerificationStatus.CANCELLED) {
                    user.setVerificationStatus(newStatus);
                } else {
                    throw new IllegalStateException(
                            "Cannot move from PENDING to " + newStatus);
                }
                break;

            case APPROVED:
                if (newStatus == VerificationStatus.CANCELLED) {
                    user.setVerificationStatus(newStatus);
                } else {
                    throw new IllegalStateException(
                            "Cannot move from APPROVED to " + newStatus);
                }
                break;

            case REJECTED:
                if (newStatus == VerificationStatus.PENDING) {
                    user.setVerificationStatus(newStatus);
                } else {
                    throw new IllegalStateException(
                            "Cannot move from REJECTED to " + newStatus);
                }
                break;

            case CANCELLED:
                throw new IllegalStateException(
                        "Cannot update a CANCELLED verification");

            default:
                throw new IllegalStateException(
                        "Unknown status: " + currentStatus);
        }

        return userRepository.save(user);
    }
}

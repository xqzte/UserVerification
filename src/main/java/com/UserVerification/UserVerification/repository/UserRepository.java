package com.UserVerification.UserVerification.repository;

import com.UserVerification.UserVerification.entity.User;
import com.UserVerification.UserVerification.enums.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findAllByVerificationStatus(VerificationStatus verificationStatus);
}
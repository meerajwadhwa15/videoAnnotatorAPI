package com.videoannotator.repository;

import com.videoannotator.model.ConfirmToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetRepository extends JpaRepository<ConfirmToken, Long> {
    Optional<ConfirmToken> findByToken(String token);
}

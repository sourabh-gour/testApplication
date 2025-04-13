package com.example.testApplication.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.testApplication.entities.PasswordResetToken;
// import com.example.testApplication.entities.User;

@Repository
public interface PasswordResetTokenRepo extends JpaRepository<PasswordResetToken, Long>{
    Optional<PasswordResetToken> findByToken(String token);
}

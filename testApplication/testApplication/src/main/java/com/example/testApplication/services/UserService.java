package com.example.testApplication.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.testApplication.entities.User;

public interface UserService extends UserDetailsService{
    User registerUser(User user);
    boolean authenticateUser(String userName, String password);
    User updateUser(User user);
    void deleteUser(Long userId);
    List<User> getAllUsers();
    boolean changePassword(String userName, String oldPassword, String newPassword);
    String generateResetToken(User user);
    boolean resetPassword(String token, String newPassword);
}

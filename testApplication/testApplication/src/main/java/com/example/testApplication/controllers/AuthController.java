package com.example.testApplication.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import com.example.testApplication.entities.AuthResponse;
import com.example.testApplication.entities.JwtResponse;
import com.example.testApplication.entities.PasswordChangeRequest;
import com.example.testApplication.entities.User;
import com.example.testApplication.repositories.UserRepo;
import com.example.testApplication.serviceImpl.UserServiceImpl;
import com.example.testApplication.services.EmailService;
// import com.example.testApplication.services.UserService;
import com.example.testApplication.utilities.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    EmailService emailService;

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody User user){
        userServiceImpl.registerUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody User authRequest) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    authRequest.getUserName(), 
                    authRequest.getPassword()
                )
            );

            final UserDetails userDetails = userServiceImpl.loadUserByUsername(authRequest.getUserName());
            final String accessToken = jwtUtil.generateToken(userDetails);
            final String refreshToken = jwtUtil.generateRefreshToken(userDetails);
            
            return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid username or password: " + e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        try {
            String token = refreshToken.substring(7);
            String username = jwtUtil.extractUsername(token);
            UserDetails userDetails = userServiceImpl.loadUserByUsername(username);
            
            // Specifically validate if it's a refresh token
            if (jwtUtil.validateRefreshToken(token)) {
                final String newAccessToken = jwtUtil.generateToken(userDetails);
                // Keep the same refresh token if it's still valid
                return ResponseEntity.ok(new JwtResponse(newAccessToken, token));
            }
            return ResponseEntity.badRequest().body("Invalid refresh token");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid token format");
        } catch (ExpiredJwtException e) {
            return ResponseEntity.badRequest().body("Refresh token has expired");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing refresh token");
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("If you see this, you're authenticated!");
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest, @RequestHeader("Authorization") String token) {
        try{
            if (!passwordChangeRequest.getNewPassword().equals(passwordChangeRequest.getConfirmPassword())) {
                return ResponseEntity.badRequest().body("New password and confirm password do not match");
            }

            String userName = jwtUtil.extractUsername(token.substring(7));
            userServiceImpl.changePassword(userName, passwordChangeRequest.getOldPassword(), passwordChangeRequest.getNewPassword());

            return ResponseEntity.ok("Password changed successfully"); 
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body("Error changing password: " + e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email){
        try{
           Optional<User> user = userServiceImpl.findByEmail(email);
           if(user.isEmpty()){
            return ResponseEntity.badRequest().body("No user found with this email");
           }

           String resetToken = userServiceImpl.generateResetToken(user.get());
           emailService.sendResetPasswordMail(user.get().getEmail(), resetToken);

           return ResponseEntity.ok("Password reset link sent to your mail");
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body("Error sending reset password mail: " + e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword){
        try{
            boolean isResetSuccessful = userServiceImpl.resetPassword(token, newPassword);
            if (isResetSuccessful) {
                return ResponseEntity.ok("Password reset successful");
            }
            return  ResponseEntity.badRequest().body("Invalid or expired token");
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body("Error resetting password: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token){
       try{
        String jwtToken = token.substring(7);
        return ResponseEntity.ok("Logged out successfully");
       }
       catch(Exception e){
        return ResponseEntity.badRequest().body("Error logging out: " + e.getMessage());
       }
    }
}

package com.example.testApplication.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.testApplication.entities.PasswordResetToken;
import com.example.testApplication.entities.User;
import com.example.testApplication.repositories.PasswordResetTokenRepo;
import com.example.testApplication.repositories.UserRepo;
import com.example.testApplication.services.EmailService;
import com.example.testApplication.services.UserService;

@Service
public class UserServiceImpl implements UserService{

    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;
    private PasswordResetTokenRepo passwordResetTokenRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder, PasswordResetTokenRepo passwordResetTokenRepo){
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetTokenRepo = passwordResetTokenRepo;
    }
    @Override
    public User registerUser(User user) {
        if(userRepo.existsByEmail(user.getEmail())){
            throw new RuntimeException("User already exists.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public boolean authenticateUser(String userName, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'authenticateUser'");
    }

    @Override
    public User updateUser(User user) {
        Optional<User> userOptional = userRepo.findById(user.getUserId());
        if(userOptional.isPresent()){
            return userRepo.save(user);
        }
        else{
            throw new RuntimeException("User not found.");
        }
    }

    @Override
    public void deleteUser(Long userId) {
        userRepo.deleteById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public boolean changePassword(String userName, String oldPassword, String newPassword) {
        Optional<User> userOptional = userRepo.findByUserName(userName);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if(!passwordEncoder.matches(oldPassword, user.getPassword())){
                throw new RuntimeException("Old password is incorrect.");
            }

            if (passwordEncoder.matches(oldPassword, newPassword)) {
                throw new RuntimeException("New password cannot be the same as the old password.");
            }

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepo.save(user);
            return true;
        }       
        throw new RuntimeException("User not found.");
    }

    @Override
    public String generateResetToken(User user) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);
        passwordResetToken.setExpirationTime(LocalDateTime.now().plusMinutes(60));
        passwordResetTokenRepo.save(passwordResetToken);
        return token;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().toString()));

        return new org.springframework.security.core.userdetails.User(
            user.getUserName(),
            user.getPassword(),
            authorities
        );
    }
    @Override
    public boolean resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> passwordResetTokenOptional = passwordResetTokenRepo.findByToken(token);
        if(passwordResetTokenOptional.isPresent()){
            PasswordResetToken passwordResetToken = passwordResetTokenOptional.get();
            if(passwordResetToken.getExpirationTime().isBefore(LocalDateTime.now())){
                passwordResetTokenRepo.delete(passwordResetToken);
                throw new RuntimeException("Link has expired.");
            } 

            User user = passwordResetToken.getUser();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepo.save(user);
            passwordResetTokenRepo.delete(passwordResetToken);

            return true;
        }
        throw new RuntimeException("Link token not found.");
    }

    public Optional<User> findByEmail(String email){
        return userRepo.findByEmail(email);
    }
}

package com.cg.service;
import com.cg.config.*;
import com.cg.entity.*;
import com.cg.repository.UserRepository;
import com.cg.service.UserService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) { return userRepository.save(user); }

    @Override
    public User findByEmail(String email) { return userRepository.findByEmail(email); }

    @Override
    public User findByUsername(String username) { return userRepository.findByUsername(username); }
    
    
    
    @Override
    public User registerUser(User user) {
        // 1) Basic validation
        if (user == null) {
            throw new IllegalArgumentException("User payload is required");
        }
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        // 2) Uniqueness check
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalStateException("Username already taken");
        }

        // 3) Password encoding
        String encoded = passwordEncoder.encode(user.getPassword());
        user.setPassword(encoded);

        // 4) Default role if absent
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        // 5) Persist and return
        return userRepository.save(user);
    }
    public User login(String username, String rawPassword) {
        if (username == null || username.isBlank() ||
            rawPassword == null || rawPassword.isBlank()) {
            return null; // or throw IllegalArgumentException
        }

        Optional<User> opt = Optional.empty();
        if (opt.isEmpty()) {
            return null; // user not found
        }

        User u = opt.get();
        // Compare raw password with the encoded password stored in DB
        boolean ok = passwordEncoder.matches(rawPassword, u.getPassword());
        return ok ? u : null;
    }

  

}
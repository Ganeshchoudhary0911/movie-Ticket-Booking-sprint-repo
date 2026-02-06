package com.cg.service;

import com.cg.dto.UserDto;
import com.cg.entity.User;
import com.cg.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto saveUser(UserDto dto) {
        User user;

        if (dto.getUserId() != null) {
            // Update existing user
            user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found: " + dto.getUserId()));
        } else {
            // Create new user
            user = new User();
        }

        // Apply allowed fields from DTO (no password here by design)
        if (dto.getUsername() != null) user.setUsername(dto.getUsername());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getRole() != null) user.setRole(dto.getRole());
        if (dto.getPhoneNumber() != null) user.setPhoneNumber(dto.getPhoneNumber());
        user.setEnabled(dto.isEnabled());

        // NOTE: Password is intentionally not handled in this DTO-based service

        User saved = userRepository.save(user);
        return toDto(saved);
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        return userRepository.findByEmail(email).map(this::toDto);
    }

    @Override
    public Optional<UserDto> findByUsername(String username) {
        return userRepository.findByUsername(username).map(this::toDto);
    }

    // ===== Inline mapping (Entity -> DTO) =====
    private UserDto toDto(User u) {
        if (u == null) return null;
        UserDto dto = new UserDto();
        dto.setUserId(u.getUserId());
        dto.setUsername(u.getUsername());
        dto.setEmail(u.getEmail());
        dto.setRole(u.getRole());
        dto.setPhoneNumber(u.getPhoneNumber());
        dto.setEnabled(u.isEnabled());
        return dto;
    }
}
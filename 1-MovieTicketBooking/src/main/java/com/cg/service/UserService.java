package com.cg.service;
 
import com.cg.dto.SignupDto;

import com.cg.dto.UserDto;

import com.cg.entity.Role;

import com.cg.entity.User;

import com.cg.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
 
import java.util.Optional;
 
@Service

public class UserService implements IUserService {
 
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Override
    @Transactional
    public UserDto saveUser(UserDto dto) {
        User user;
        if (dto.getUserId() != null) {
            user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found: " + dto.getUserId()));
        } else {
            user = new User();
        }
        if (dto.getUsername() != null) user.setUsername(dto.getUsername());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getRole() != null) user.setRole(dto.getRole());
        if (dto.getPhoneNumber() != null) user.setPhoneNumber(dto.getPhoneNumber());
        user.setEnabled(dto.isEnabled());
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
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**

     * Creates a new user from SignupDto, encodes the password,

     * sets defaults, and saves via repository.

     */

    @Override

    @Transactional

    public UserDto createUser(SignupDto signupDto) {

        User user = new User();

        user.setUsername(signupDto.getUsername());

        user.setEmail(signupDto.getEmail());

        user.setPassword(encoder.encode(signupDto.getPassword()));

        user.setPhoneNumber(signupDto.getPhoneNumber());
 
        // Defaults

        user.setRole(Role.USER);

        user.setEnabled(true);
 
        User saved = userRepository.save(user);

        return toDto(saved);

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
    @Override
    public Optional<User> findEntityByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findEntityByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findEntityByUsernameOrEmail(String login) {
        Optional<User> u = userRepository.findByUsername(login);
        return u.isPresent() ? u : userRepository.findByEmail(login);
    }

    @Override
    @Transactional
    public User saveEntity(User user) {
        return userRepository.save(user);
    }
}

 
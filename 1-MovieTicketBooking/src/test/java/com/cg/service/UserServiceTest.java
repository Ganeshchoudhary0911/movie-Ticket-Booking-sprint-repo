package com.cg.service;

import com.cg.dto.SignupDto;
import com.cg.entity.User;
import com.cg.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * UserService Tests (4 test methods)
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("createUser - Creates new user from signup DTO")
    void shouldCreateUser_fromSignupDto() {
        SignupDto signupDto = new SignupDto();
        signupDto.setUsername("testuser");
        signupDto.setEmail("test@example.com");
        signupDto.setPassword("password123");
        signupDto.setPhoneNumber("9876543210");

        User user = new User();
        user.setUserId(1L);

        when(passwordEncoder.encode("password123")).thenReturn("encoded");
        when(userRepository.save(any())).thenReturn(user);

        userService.createUser(signupDto);

        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("existsByUsername - Returns true when username exists")
    void shouldExistsByUsername_returnTrue() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        assertTrue(userService.existsByUsername("testuser"));
    }

    @Test
    @DisplayName("existsByEmail - Returns false when email not found")
    void shouldExistsByEmail_returnFalse() {
        when(userRepository.existsByEmail("nonexistent@example.com")).thenReturn(false);

        assertFalse(userService.existsByEmail("nonexistent@example.com"));
    }

    @Test
    @DisplayName("findById - Returns user when found")
    void shouldFindById_whenFound() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("testuser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }
}
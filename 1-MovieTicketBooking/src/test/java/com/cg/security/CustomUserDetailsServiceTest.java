package com.cg.security;

import com.cg.entity.Role;
import com.cg.entity.User;
import com.cg.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * CustomUserDetailsService Unit Tests (2 methods)
 * 
 * Tests:
 * 3) shouldLoadUserDetails_whenUserExists
 * 4) shouldThrowException_whenUserNotFound
 */
@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setUsername("admin");
        testUser.setEmail("admin@example.com");
        testUser.setPassword("$2a$10$hashedPassword");
        testUser.setRole(Role.ADMIN);
        testUser.setEnabled(true);
    }

    @Test
    @DisplayName("shouldLoadUserDetails_whenUserExists")
    void shouldLoadUserDetails_whenUserExists() {
        // Arrange
        when(userRepository.findByUsername("admin"))
                .thenReturn(Optional.of(testUser));

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("admin");

        // Assert
        assertNotNull(userDetails);
        assertEquals("admin", userDetails.getUsername());
        assertEquals("$2a$10$hashedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    @DisplayName("shouldThrowException_whenUserNotFound")
    void shouldThrowException_whenUserNotFound() {
        // Arrange
        when(userRepository.findByUsername(anyString()))
                .thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("nonexistent"));
    }
}
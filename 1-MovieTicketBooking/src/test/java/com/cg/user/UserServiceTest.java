package com.cg.user;

import com.cg.dto.UserDto;
import com.cg.entity.Role;
import com.cg.entity.User;
import com.cg.repository.UserRepository;
import com.cg.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User existingUser;

    @BeforeEach
    void setup() {
        existingUser = new User();
        existingUser.setUserId(1L);
        existingUser.setUsername("Alice");
        existingUser.setEmail("alice@example.com");
        existingUser.setRole(Role.USER);
        existingUser.setPhoneNumber("9999999999");
        existingUser.setEnabled(true);
        
        System.out.println("User service testcases");
    }

    @Test
    void saveUser_createsNewUser_whenIdIsNull() {
        // Arrange
        UserDto dto = new UserDto();
        dto.setUserId(null);
        dto.setUsername("Bob");
        dto.setEmail("bob@example.com");
        dto.setRole(Role.ADMIN);
        dto.setPhoneNumber("8888888888");
        dto.setEnabled(true);

        // The repository will assign an ID and return entity
        User savedEntity = new User();
        savedEntity.setUserId(10L);
        savedEntity.setUsername("Bob");
        savedEntity.setEmail("bob@example.com");
        savedEntity.setRole(Role.ADMIN);
        savedEntity.setPhoneNumber("8888888888");
        savedEntity.setEnabled(true);

        when(userRepository.save(any(User.class))).thenReturn(savedEntity);

        // Act
        UserDto result = userService.saveUser(dto);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getUserId());
        assertEquals("Bob", result.getUsername());
        assertEquals("bob@example.com", result.getEmail());
        assertEquals(Role.ADMIN, result.getRole());
        assertEquals("8888888888", result.getPhoneNumber());
        assertTrue(result.isEnabled());

        // Verify we saved with values coming from DTO
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User toSave = captor.getValue();
        assertNull(toSave.getUserId()); // new user creation path
        assertEquals("Bob", toSave.getUsername());
        assertEquals("bob@example.com", toSave.getEmail());
    }

    @Test
    void saveUser_partialUpdate_doesNotOverwriteNullFields() {
        // Arrange: existing user in DB
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        // Only update email; leave other fields null to ensure they don't change.
        UserDto dto = new UserDto();
        dto.setUserId(1L);
        dto.setEmail("new.email@example.com");
        // IMPORTANT: enabled is primitive boolean in the DTO, so we must set it
        // explicitly to keep current value unless you change DTO to Boolean.
        dto.setEnabled(true);

        User savedEntity = new User();
        savedEntity.setUserId(1L);
        savedEntity.setUsername("Alice"); // unchanged
        savedEntity.setEmail("new.email@example.com"); // changed
        savedEntity.setRole(Role.USER); // unchanged
        savedEntity.setPhoneNumber("9999999999"); // unchanged
        savedEntity.setEnabled(true); // same as before

        when(userRepository.save(any(User.class))).thenReturn(savedEntity);

        // Act
        UserDto result = userService.saveUser(dto);

        // Assert
        assertEquals(1L, result.getUserId());
        assertEquals("Alice", result.getUsername(), "Username should remain unchanged");
        assertEquals("new.email@example.com", result.getEmail(), "Email should be updated");
        assertEquals(Role.USER, result.getRole(), "Role should remain unchanged");
        assertEquals("9999999999", result.getPhoneNumber(), "Phone should remain unchanged");
        assertTrue(result.isEnabled());
    }

    @Test
    void saveUser_updateThrows_whenUserNotFound() {
        // Arrange
        when(userRepository.findById(42L)).thenReturn(Optional.empty());

        UserDto dto = new UserDto();
        dto.setUserId(42L);
        dto.setUsername("Ghost");
        dto.setEnabled(true);

        // Act & Assert
        RuntimeException ex =
                assertThrows(RuntimeException.class, () -> userService.saveUser(dto));
        assertTrue(ex.getMessage().contains("User not found: 42"));

        verify(userRepository).findById(42L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findByEmail_returnsMappedDto() {
        // Arrange
        String email = "alice@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        // Act
        Optional<UserDto> found = userService.findByEmail(email);

        // Assert
        assertTrue(found.isPresent());
        assertEquals("Alice", found.get().getUsername());
        assertEquals(email, found.get().getEmail());
    }

    @Test
    void findByUsername_returnsEmpty_whenNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        Optional<UserDto> found = userService.findByUsername("unknown");

        assertTrue(found.isEmpty());
    }

    @Test
    void saveUser_enabledIsAlwaysAppliedFromDto_currentBehavior() {
        // Arrange: existing user is enabled=true
        assertTrue(existingUser.isEnabled());
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        UserDto dto = new UserDto();
        dto.setUserId(1L);
        dto.setEnabled(false); // will overwrite to false

        User savedEntity = new User();
        savedEntity.setUserId(1L);
        savedEntity.setEnabled(false);
        savedEntity.setUsername(existingUser.getUsername());
        savedEntity.setEmail(existingUser.getEmail());
        savedEntity.setRole(existingUser.getRole());
        savedEntity.setPhoneNumber(existingUser.getPhoneNumber());

        when(userRepository.save(any(User.class))).thenReturn(savedEntity);

        // Act
        UserDto result = userService.saveUser(dto);

        // Assert
        assertFalse(result.isEnabled(), "Enabled should be overwritten by DTO's value");
    }
}
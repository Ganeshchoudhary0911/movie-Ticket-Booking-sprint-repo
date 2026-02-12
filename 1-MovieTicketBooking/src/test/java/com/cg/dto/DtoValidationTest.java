package com.cg.dto;

import com.cg.entity.Role;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DTO Validation Tests (3 test methods)
 * Validates all constraint annotations across DTOs
 */
@SpringBootTest
class DtoValidationTest {

    @Autowired
    private Validator validator;

    // ====== MovieDto ======
    @DisplayName("MovieDto: Valid movie passes validation")
    @ParameterizedTest
    @ValueSource(doubles = {0.0, 5.0, 10.0})
    void shouldValidateMovieDto_validRating(double rating) {
        MovieDto dto = new MovieDto();
        dto.setMovieName("Avatar");
        dto.setGenre("Science Fiction");
        dto.setLanguage("English");
        dto.setDuration(162);
        dto.setRating(rating);
        dto.setPosterUrl("https://example.com/poster.jpg");

        Set<ConstraintViolation<MovieDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Valid movie should have no violations");
    }

    // ====== UserDto ======
    @DisplayName("UserDto: Valid user passes validation")
    @ParameterizedTest
    @ValueSource(strings = {"user123", "john_doe", "jane.smith"})
    void shouldValidateUserDto_validUsername(String username) {
        UserDto dto = new UserDto();
        dto.setUsername(username);
        dto.setEmail("test@example.com");
        dto.setPhoneNumber("9876543210");
        dto.setRole(Role.USER);
        dto.setEnabled(true);

        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Valid user should have no violations");
    }

    // ====== TheatreDto ======
    @DisplayName("TheatreDto: Valid theatre passes validation")
    @ParameterizedTest
    @ValueSource(ints = {50, 500, 1000})
    void shouldValidateTheatreDto_validSeats(int seats) {
        TheatreDto dto = new TheatreDto();
        dto.setTheatreName("PVR Cinemas");
        dto.setLocation("Downtown");
        dto.setCity("Mumbai");
        dto.setTotalSeats(seats);
        dto.setContactNumber("9876543210");

        Set<ConstraintViolation<TheatreDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Valid theatre should have no violations");
    }
}
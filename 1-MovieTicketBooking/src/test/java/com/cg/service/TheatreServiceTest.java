package com.cg.service;

import com.cg.dto.TheatreDto;
import com.cg.entity.Theatre;
import com.cg.repository.TheatreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * TheatreService Tests (5 test methods)
 */
@ExtendWith(MockitoExtension.class)
class TheatreServiceTest {

    @Mock
    private TheatreRepository theatreRepository;

    @InjectMocks
    private TheatreService theatreService;

    @Test
    @DisplayName("getAllTheatres - Returns all theatres")
    void shouldGetAllTheatres() {
        Theatre theatre = new Theatre();
        theatre.setTheatreId(1L);
        theatre.setTheatreName("PVR");

        when(theatreRepository.findAll()).thenReturn(List.of(theatre));

        List<TheatreDto> result = theatreService.getAllTheatres();

        assertEquals(1, result.size());
        assertEquals("PVR", result.get(0).getTheatreName());
    }

    @Test
    @DisplayName("getAllTheatres - Returns empty when no theatres")
    void shouldGetAllTheatres_returnEmpty() {
        when(theatreRepository.findAll()).thenReturn(Collections.emptyList());

        List<TheatreDto> result = theatreService.getAllTheatres();

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("getTheatresByCity - Filters theatres by city")
    void shouldGetTheatresByCity() {
        Theatre theatre = new Theatre();
        theatre.setTheatreId(1L);
        theatre.setCity("Mumbai");

        when(theatreRepository.findByCityIgnoreCase("Mumbai")).thenReturn(List.of(theatre));

        List<TheatreDto> result = theatreService.getTheatresByCity("Mumbai");

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("getTheatreById - Returns theatre when found")
    void shouldGetTheatreById_whenFound() {
        Theatre theatre = new Theatre();
        theatre.setTheatreId(1L);

        when(theatreRepository.findById(1L)).thenReturn(Optional.of(theatre));

        TheatreDto result = theatreService.getTheatreById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getTheatreId());
    }

    @Test
    @DisplayName("deleteTheatre - Deletes theatre by ID")
    void shouldDeleteTheatre() {
        theatreService.deleteTheatre(1L);

        verify(theatreRepository).deleteById(1L);
    }
}
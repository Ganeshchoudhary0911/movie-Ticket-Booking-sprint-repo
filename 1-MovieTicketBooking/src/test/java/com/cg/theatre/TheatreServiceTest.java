package com.cg.theatre;

import com.cg.dto.TheatreDto;
import com.cg.entity.Theatre;
import com.cg.repository.TheatreRepository;
import com.cg.service.TheatreService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TheatreServiceTest {

    @Mock
    private TheatreRepository theatreRepository;

    @InjectMocks
    private TheatreService theatreService;

    @Test
    void testSaveTheatre_creates_whenIdNull() {
        // Arrange: input DTO (create new)
        TheatreDto input = new TheatreDto();
        input.setTheatreName("PVR");
        input.setLocation("Hitech City");
        input.setCity("Hyderabad");
        input.setTotalSeats(250);          // If your DTO uses Integer, set Integer.valueOf(250)
        input.setContactNumber("9999999999");

        // Mock repository save result (entity back from DB)
        Theatre savedEntity = new Theatre();
        savedEntity.setTheatreId(10L);
        savedEntity.setTheatreName("PVR");
        savedEntity.setLocation("Hitech City");
        savedEntity.setCity("Hyderabad");
        savedEntity.setTotalSeats(250);
        savedEntity.setContactNumber("9999999999");

        when(theatreRepository.save(any(Theatre.class))).thenReturn(savedEntity);

        // Act
        TheatreDto result = theatreService.saveTheatre(input);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getTheatreId());
        assertEquals("PVR", result.getTheatreName());
        assertEquals("Hyderabad", result.getCity());
        assertEquals(250, result.getTotalSeats());
        assertEquals("9999999999", result.getContactNumber());

        // Also verify we mapped the DTO into an entity before saving
        ArgumentCaptor<Theatre> captor = ArgumentCaptor.forClass(Theatre.class);
        verify(theatreRepository, times(1)).save(captor.capture());
        Theatre toSave = captor.getValue();
        assertNull(toSave.getTheatreId()); // create path should not set id
        assertEquals("PVR", toSave.getTheatreName());
        assertEquals("Hitech City", toSave.getLocation());
        assertEquals("Hyderabad", toSave.getCity());
        assertEquals(250, toSave.getTotalSeats());
        assertEquals("9999999999", toSave.getContactNumber());
    }

    @Test
    void testGetAllTheatres() {
        // Arrange: repository returns entities
        Theatre t = new Theatre();
        t.setTheatreId(1L);
        t.setTheatreName("PVR");
        t.setLocation("Hitech City");
        t.setCity("Hyderabad");
        t.setTotalSeats(200);
        t.setContactNumber("1234567890");

        when(theatreRepository.findAll()).thenReturn(Arrays.asList(t));

        // Act
        var list = theatreService.getAllTheatres();

        // Assert
        assertEquals(1, list.size());
        TheatreDto dto = list.get(0);
        assertEquals(1L, dto.getTheatreId());
        assertEquals("PVR", dto.getTheatreName());
        assertEquals("Hyderabad", dto.getCity());

        verify(theatreRepository).findAll();
    }

    @Test
    void testGetTheatreById() {
        // Arrange
        Theatre t = new Theatre();
        t.setTheatreId(1L);
        t.setTheatreName("INOX");
        t.setLocation("Madhapur");
        t.setCity("Hyderabad");
        t.setTotalSeats(180);
        t.setContactNumber("1112223334");

        when(theatreRepository.findById(1L)).thenReturn(Optional.of(t));

        // Act
        TheatreDto dto = theatreService.getTheatreById(1L);

        // Assert
        assertNotNull(dto);
        assertEquals(1L, dto.getTheatreId());
        assertEquals("INOX", dto.getTheatreName());
        assertEquals("Hyderabad", dto.getCity());

        verify(theatreRepository).findById(1L);
    }
}
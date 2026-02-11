package com.cg.theatre;

import com.cg.controller.TheatreController;
import com.cg.dto.TheatreDto;
import com.cg.service.TheatreService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

// Static imports for Mockito and MockMvc assertions
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TheatreController.class)
@AutoConfigureMockMvc(addFilters = false) 
class TheatreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TheatreService theatreService;

    @Test
    @DisplayName("GET /theatres should return theatre-list view")
    void testGetAllTheatres() throws Exception {
        // 1. Prepare Mock Data
        List<TheatreDto> theatres = List.of(
                new TheatreDto(1L, "PVR", "City Center", "Delhi", 200, "9999")
        );

        // 2. Define Mock Behavior
        when(theatreService.getAllTheatres()).thenReturn(theatres);

        // 3. Perform Request and Verify View and Model
        mockMvc.perform(get("/theatres"))
                .andExpect(status().isOk())
                // Use view() and model() to verify HTML-based controllers
                .andExpect(view().name("theatre-list"))
                .andExpect(model().attributeExists("theatres"));
    }

    @Test
    @DisplayName("POST /admin/theatres/add should redirect to admin list")
    void testAddTheatre() throws Exception {
        // Use flashAttr to simulate @ModelAttribute form submission
        TheatreDto input = new TheatreDto(null, "PVR", "City Center", "Delhi", 200, "9999999999");
        
        mockMvc.perform(post("/admin/theatres/add") 
                .flashAttr("theatre", input)) 
                .andExpect(status().is3xxRedirection()) // Verify redirection status (302)
                .andExpect(redirectedUrl("/admin/theatres"));

        // Verify that the service method was called exactly once
        verify(theatreService, times(1)).addTheatre(any(TheatreDto.class));
    }
}

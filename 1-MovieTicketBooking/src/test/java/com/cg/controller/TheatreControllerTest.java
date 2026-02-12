package com.cg.controller;

import com.cg.dto.TheatreDto;
import com.cg.service.TheatreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * TheatreController Tests (4 test methods)
 */
@WebMvcTest(TheatreController.class)
class TheatreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TheatreService theatreService;

    private TheatreDto validTheatre;

    @BeforeEach
    void setUp() {
        validTheatre = new TheatreDto();
        validTheatre.setTheatreName("PVR Cinemas");
        validTheatre.setLocation("Downtown Plaza");
        validTheatre.setCity("Mumbai");
        validTheatre.setTotalSeats(200);
        validTheatre.setContactNumber("9876543210");
    }
//
//    @Test
//    @DisplayName("GET /theatres - Returns theatre list")
//    void shouldGetTheatres_returnList() throws Exception {
//        when(theatreService.getAllTheatres()).thenReturn(List.of(validTheatre));
//
//        mockMvc.perform(get("/theatres"))
//                .andExpect(view().name("theatre-list"))
//                .andExpect(model().attributeExists("theatres"));
//
//        verify(theatreService).getAllTheatres();
//    }

    @Test
    @DisplayName("POST /admin/theatres - Valid theatre redirects")
    @WithMockUser(roles = "ADMIN")
    void shouldAddTheatre_withValidData_redirect() throws Exception {
        mockMvc.perform(post("/admin/theatres")
                .with(csrf())
                .param("theatreName", validTheatre.getTheatreName())
                .param("location", validTheatre.getLocation())
                .param("city", validTheatre.getCity())
                .param("totalSeats", String.valueOf(validTheatre.getTotalSeats()))
                .param("contactNumber", validTheatre.getContactNumber()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/theatres"));

        verify(theatreService).addTheatre(any(TheatreDto.class));
    }

    @Test
    @DisplayName("POST /admin/theatres - Invalid theatre returns form")
    @WithMockUser(roles = "ADMIN")
    void shouldAddTheatre_withInvalidData_returnForm() throws Exception {
        mockMvc.perform(post("/admin/theatres")
                .with(csrf())
                .param("theatreName", "")
                .param("location", "Location")
                .param("city", "City")
                .param("totalSeats", "200")
                .param("contactNumber", "9876543210"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin-theatre-form"));

        verify(theatreService, never()).addTheatre(any());
    }
}
package com.cg.theatre;

import com.cg.controller.TheatreController;
import com.cg.entity.Theatre;
import com.cg.service.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TheatreController.class)
class TheatreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TheatreService theatreService;

    @Test
    @DisplayName("GET /theatres - should return all theatres")
    void testGetAllTheatres() throws Exception {
        when(theatreService.getAllTheatres()).thenReturn(Arrays.asList(
                new Theatre("PVR", "City Center"),
                new Theatre("INOX", "Mall Road")
        ));

        mockMvc.perform(get("/theatres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("POST /theatres - should add a theatre")
    void testAddTheatre() throws Exception {
        Theatre theatre = new Theatre("PVR", "City Center");
        when(theatreService.saveTheatre(Mockito.any(Theatre.class))).thenReturn(theatre);

        mockMvc.perform(post("/theatres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"PVR\", \"location\":\"City Center\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("PVR"));
    }
}
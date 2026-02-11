package com.cg.theatre;

import com.cg.controller.TheatreController;
import com.cg.dto.TheatreDto;
import com.cg.service.TheatreService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

// Static imports for Mockito and MockMvc assertions
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TheatreControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TheatreService theatreService;

    @InjectMocks
    private TheatreController theatreController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(theatreController)
                .build();
    }

    @Test
    void testGetAllTheatres() throws Exception {

        TheatreDto dto = new TheatreDto();
        dto.setTheatreId(1L);

        when(theatreService.getAllTheatres()).thenReturn(List.of(dto));

        mockMvc.perform(get("/theatres"))
                .andExpect(status().isOk())
                .andExpect(view().name("theatre-list"))
                .andExpect(model().attributeExists("theatres"));

        verify(theatreService).getAllTheatres();
    }

    @Test
    void testAddTheatre() throws Exception {

        mockMvc.perform(post("/admin/theatres")
                .flashAttr("theatre", new TheatreDto()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/theatres"));

        verify(theatreService).addTheatre(any(TheatreDto.class));
    }
}

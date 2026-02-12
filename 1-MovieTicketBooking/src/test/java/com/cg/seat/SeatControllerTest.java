package com.cg.seat;

import com.cg.controller.SeatController;
import com.cg.dto.ShowDto;
import com.cg.service.SeatService;
import com.cg.service.ShowService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SeatController.class)
@AutoConfigureMockMvc(addFilters = false)
class SeatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeatService seatService;

    @MockBean
    private ShowService showService;


    // ==============================
    // GET seats page
    // ==============================
    @Test
    void testShowSeats() throws Exception {

        ShowDto dto = new ShowDto();
        dto.setMovieName("Movie");
        dto.setTheatreName("Theatre");
        dto.setShowDate(LocalDate.now());
        dto.setShowTime(LocalTime.NOON);

        when(showService.getShowById(1L)).thenReturn(dto);
        when(seatService.getBookedSeats(1L))
                .thenReturn(Arrays.asList("A1", "A2"));

        mockMvc.perform(get("/seats/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("seat-selection"));
    }
}

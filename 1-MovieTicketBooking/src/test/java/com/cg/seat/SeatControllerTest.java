package com.cg.seat;

import com.cg.controller.SeatController;
import com.cg.dto.SeatDto;
import com.cg.service.SeatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SeatController.class)
class SeatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeatService seatService;

    private SeatDto seatDto(long id, String num, String row, String type, double price, boolean booked, Long showId) {
        SeatDto d = new SeatDto();
        d.setSeatId(id);
        d.setSeatNumber(num);
        d.setSeatRow(row);
        d.setSeatType(type);
        d.setSeatPrice(price);
        d.setBooked(booked);
        d.setShowId(showId);
        return d;
    }

    @Test
    @DisplayName("GET /api/seats/show/{showId} - returns all seats for a show")
    void testGetSeatsByShow() throws Exception {
        Long showId = 100L;
        List<SeatDto> list = Arrays.asList(
                seatDto(1L, "A1", "A", "VIP", 250.0, false, showId),
                seatDto(2L, "A2", "A", "VIP", 250.0, true, showId)
        );
        when(seatService.getSeatsByShow(showId)).thenReturn(list);

        mockMvc.perform(get("/api/seats/show/{showId}", showId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].seatNumber").value("A1"))
                .andExpect(jsonPath("$[0].booked").value(false))
                .andExpect(jsonPath("$[1].seatNumber").value("A2"))
                .andExpect(jsonPath("$[1].booked").value(true));
    }

    @Test
    @DisplayName("GET /api/seats/{seatId} - returns one seat")
    void testGetSeatById() throws Exception {
        Long seatId = 1L;
        SeatDto dto = seatDto(seatId, "B5", "B", "REGULAR", 180.0, false, 200L);
        when(seatService.getSeatById(seatId)).thenReturn(dto);

        mockMvc.perform(get("/api/seats/{seatId}", seatId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.seatId").value(1))
                .andExpect(jsonPath("$.seatNumber").value("B5"))
                .andExpect(jsonPath("$.seatRow").value("B"))
                .andExpect(jsonPath("$.seatType").value("REGULAR"))
                .andExpect(jsonPath("$.seatPrice").value(180.0))
                .andExpect(jsonPath("$.booked").value(false))
                .andExpect(jsonPath("$.showId").value(200));
    }

    @Test
    @DisplayName("PATCH /api/seats/{seatId}/book - marks a seat as booked")
    void testMarkSeatAsBooked() throws Exception {
        Long seatId = 5L;
        SeatDto booked = seatDto(seatId, "C3", "C", "RECLINER", 350.0, true, 300L);
        when(seatService.markSeatAsBooked(seatId)).thenReturn(booked);

        mockMvc.perform(patch("/api/seats/{seatId}/book", seatId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seatId").value(5))
                .andExpect(jsonPath("$.seatNumber").value("C3"))
                .andExpect(jsonPath("$.booked").value(true));
    }

    @Test
    @DisplayName("GET /api/seats/{showId}/booked - returns booked seat numbers")
    void testGetBookedSeats() throws Exception {
        Long showId = 400L;
        when(seatService.getBookedSeats(showId)).thenReturn(Arrays.asList("A1", "A2", "B4"));

        mockMvc.perform(get("/api/seats/{showId}/booked", showId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0]").value("A1"))
                .andExpect(jsonPath("$[1]").value("A2"))
                .andExpect(jsonPath("$[2]").value("B4"));
    }
}
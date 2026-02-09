package com.cg.booking;

import com.cg.controller.BookingController;
import com.cg.dto.BookingDto;
import com.cg.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean BookingService bookingService;
    @MockBean ShowService showService;
    @MockBean UserService userService;

    // ================= PAYMENT PAGE =================
    @Test
    void paymentPage_shouldReturnView() throws Exception {

        mockMvc.perform(get("/payment")
                .param("seats","2")
                .param("amount","500"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment"));
    }

    // ================= SUCCESS PAGE =================
    @Test
    void successPage_shouldReturnViewWithBooking() throws Exception {

        BookingDto dto = new BookingDto();
        dto.setBookingId(1L);
        dto.setBookingDate(LocalDate.now());

        when(bookingService.getBookingById(1L)).thenReturn(dto);

        mockMvc.perform(get("/booking/success")
                .param("bookingId","1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("booking"))
                .andExpect(view().name("success"));
    }

    // ================= HISTORY PAGE =================
    @Test
    void history_shouldReturnView() throws Exception {

        when(userService.findByUsername("anshu"))
                .thenReturn(Optional.of(new com.cg.dto.UserDto()));

        when(bookingService.getUserBookings(1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/history")
                .principal(() -> "anshu"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookings-history"));
    }
}
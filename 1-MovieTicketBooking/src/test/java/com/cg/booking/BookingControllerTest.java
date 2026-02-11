package com.cg.booking;

import com.cg.controller.BookingController;
import com.cg.dto.BookingDto;
import com.cg.dto.UserDto;
import com.cg.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
@AutoConfigureMockMvc(addFilters = false) // Disables Spring Security filters for easier testing
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private BookingService bookingService;
    @MockBean private ShowService showService;
    @MockBean private UserService userService;
    @MockBean private PaymentService paymentService; // Required for constructor injection

    // ================= PAYMENT ENTRY (FORWARD TEST) =================
    @Test
    void paymentEntry_shouldForwardToDtoPayment() throws Exception {
        // Path matches @GetMapping("/payment")
        mockMvc.perform(get("/payment")
                .param("bookingId", "101"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/payment/dto?bookingId=101"));
    }

    // ================= SUCCESS PAGE =================
    @Test
    void successPage_shouldReturnViewWithBooking() throws Exception {
        BookingDto dto = new BookingDto();
        dto.setBookingId(1L);
        dto.setBookingDate(LocalDate.now());

        // Path matches @GetMapping("/success/{bookingId}")
        when(bookingService.getBookingById(1L)).thenReturn(dto);

        mockMvc.perform(get("/success/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("booking"))
                .andExpect(view().name("success"));
    }

    // ================= HISTORY PAGE =================
    @Test
    void history_shouldReturnView() throws Exception {
        UserDto mockUser = new UserDto();
        mockUser.setUserId(1L);

        // Controller logic: checks username, then email
        when(userService.findByUsername("anshu")).thenReturn(Optional.of(mockUser));
        when(bookingService.getUserBookings(1L)).thenReturn(List.of());

        mockMvc.perform(get("/history")
                .principal(() -> "anshu")) // Mocks authenticated user principal
                .andExpect(status().isOk())
                .andExpect(view().name("bookings-history"))
                .andExpect(model().attributeExists("bookings"));
    }
}


package com.cg.controller;

import com.cg.dto.BookingDto;
import com.cg.service.BookingService;
import com.cg.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private PaymentService paymentService;

    // ✅ POSITIVE: Show payment page
    @Test
    @WithMockUser
    void testSelectMethod_Success() throws Exception {
        BookingDto mockBooking = new BookingDto();
        when(bookingService.getBookingById(1L)).thenReturn(mockBooking);

        mockMvc.perform(get("/payment/method").param("bookingId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment-method"))
                .andExpect(model().attributeExists("booking"));
    }

    // ✅ POSITIVE: Process payment
    @Test
    @WithMockUser
    void testChooseAndPay_Success() throws Exception {
        mockMvc.perform(post("/payment/choose")
                .param("bookingId", "1")
                .param("method", "UPI"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/success/1"))
                .andExpect(flash().attribute("paidBy", "UPI"));
    }

    // ❌ NEGATIVE: Booking not found
    @Test
    @WithMockUser
    void testSelectMethod_NotFound() throws Exception {
        when(bookingService.getBookingById(99L)).thenReturn(null);

        mockMvc.perform(get("/payment/method").param("bookingId", "99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/history?error=bookingNotFound"));
    }
}

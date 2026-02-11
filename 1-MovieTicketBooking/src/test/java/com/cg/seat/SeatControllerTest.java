package com.cg.seat;

import com.cg.controller.SeatController;
import com.cg.dto.ShowDto;
import com.cg.service.SeatService;
import com.cg.service.ShowService;
import com.cg.service.MovieService;
import com.cg.service.TheatreService;
import com.cg.repository.BookingRepository;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.test.context.TestConfiguration;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = SeatController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SeatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeatService seatService;

    @MockBean
    private ShowService showService;

    @MockBean
    private MovieService movieService;

    @MockBean
    private TheatreService theatreService;

    @MockBean
    private BookingRepository bookingRepository;

    // Required for resolving Thymeleaf/JSP views
    @TestConfiguration
    static class TestConfig {

        @Bean
        public ViewResolver viewResolver() {
            InternalResourceViewResolver resolver = new InternalResourceViewResolver();
            resolver.setPrefix("/WEB-INF/views/");
            resolver.setSuffix(".html");
            return resolver;
        }
    }

    @Test
    void testShowSeats() throws Exception {

        // Mock DTO
        ShowDto dto = new ShowDto();
        dto.setMovieName("Movie");
        dto.setTheatreName("Theatre");
        dto.setShowDate(LocalDate.parse("2026-02-10"));
        dto.setShowTime(LocalTime.parse("18:00"));

        Mockito.when(showService.getShowById(1L)).thenReturn(dto);
        Mockito.when(bookingRepository.findBookedSeatsByShowId(1L))
                .thenReturn(Arrays.asList("A1", "A2"));

        mockMvc.perform(get("/seats/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("seat-selection"));
    }

    @Test
    void testBookSeat() throws Exception {
        mockMvc.perform(post("/seats/book/1"))
                .andExpect(status().is3xxRedirection());
    }
}
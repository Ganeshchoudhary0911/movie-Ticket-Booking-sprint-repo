package com.cg.seat;

import com.cg.dto.SeatDto;
import com.cg.entity.Seat;
import com.cg.entity.Show;
import com.cg.repository.BookingRepository;
import com.cg.repository.SeatRepository;
import com.cg.repository.ShowRepository;
import com.cg.service.SeatService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private ShowRepository showRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private SeatService seatService;

    // ---- IMPORTANT: make them fields ----
    private Show show;
    private Seat seat;

    @BeforeEach
    void setup() {
        show = new Show();
        show.setShowId(100L);

        seat = new Seat();
        seat.setSeatId(1L);
        seat.setSeatNumber("A1");
        seat.setSeatRow("A");
        seat.setSeatType("VIP");
        seat.setSeatPrice(200.0);
        seat.setBooked(false);
        seat.setShow(show);
    }

    @Test
    void testGetSeatById() {
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));

        SeatDto dto = seatService.getSeatById(1L);

        assertEquals(1L, dto.getSeatId());
        assertEquals("A1", dto.getSeatNumber());
        assertEquals("A", dto.getSeatRow());
        assertEquals(100L, dto.getShowId());
    }

    @Test
    void testMarkSeatAsBooked() {
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));
        when(seatRepository.save(any(Seat.class))).thenAnswer(inv -> inv.getArgument(0));

        SeatDto dto = seatService.markSeatAsBooked(1L);

        assertTrue(dto.isBooked());
        // Verify the same instance was saved with booked=true
        verify(seatRepository).save(seat);
        assertTrue(seat.isBooked());
    }
}

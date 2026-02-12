package com.cg.service;

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

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    private Seat seat;
    private Show show;

    // ================= SETUP =================
    @BeforeEach
    void setup() {
        show = new Show();
        show.setShowId(100L);

        seat = new Seat();
        seat.setSeatId(1L);
        seat.setSeatRow("A");
        seat.setSeatNumber("1");
        seat.setSeatType("VIP");
        seat.setSeatPrice(200.0);
        seat.setBooked(false);
        seat.setShow(show);
    }

    // ================= getSeatById =================
    @Test
    void testGetSeatById() {

        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));

        SeatDto dto = seatService.getSeatById(1L);

        assertEquals(1L, dto.getSeatId());
        assertEquals("A", dto.getSeatRow());
        assertEquals(100L, dto.getShowId());
    }

    // ================= getSeatsByShow =================
    @Test
    void testGetSeatsByShow() {

        when(seatRepository.findByShowShowId(100L)).thenReturn(List.of(seat));

        List<SeatDto> list = seatService.getSeatsByShow(100L);

        assertEquals(1, list.size());
        assertEquals("A", list.get(0).getSeatRow());
    }

    // ================= bookSeats (multi booking) =================
    @Test
    void testBookSeats() {

        when(seatRepository.findBySeatIdIn(List.of(1L)))
                .thenReturn(List.of(seat));

        when(seatRepository.saveAll(any()))
                .thenReturn(List.of(seat));

        Set<Seat> result = seatService.bookSeats(List.of(1L));

        assertTrue(seat.isBooked());
        assertEquals(1, result.size());

        verify(seatRepository).saveAll(any());
    }

    // ================= getBookedSeats =================
    @Test
    void testGetBookedSeats() {

        Seat bookedSeat = new Seat();
        bookedSeat.setSeatRow("A");
        bookedSeat.setSeatNumber("1");

        when(seatRepository.findBookedSeatsByShowId(100L))
                .thenReturn(List.of(bookedSeat));

        List<String> result = seatService.getBookedSeats(100L);

        assertEquals("A1", result.get(0));
    }
}

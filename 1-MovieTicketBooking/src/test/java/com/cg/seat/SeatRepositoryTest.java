package com.cg.seat;

import com.cg.entity.Seat;
import com.cg.entity.Show;
import com.cg.repository.SeatRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SeatRepositoryTest {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Show persistShow() {
        Show show = new Show();
        show.setShowDate(LocalDate.of(2026, 2, 1));
        show.setShowTime(LocalTime.of(18, 30));
        show.setPrice(250.0);
        // If your Show requires associations (movie, theatre) as non-null,
        // you must create and persist those here as well.
        return entityManager.persistAndFlush(show);
    }

    private Seat buildSeat(String number, String row, String type, double price, boolean booked, Show show) {
        Seat s = new Seat();
        s.setSeatNumber(number);
        s.setSeatRow(row);
        s.setSeatType(type);
        s.setSeatPrice(price);
        s.setBooked(booked);
        s.setShow(show); // safe even if optional=false
        return s;
    }

    @Test
    void testSaveAndFindSeat() {
        Show show = persistShow();

        Seat seat = buildSeat("A1", "A", "VIP", 200.0, false, show);
        Seat saved = seatRepository.save(seat);
        assertNotNull(saved.getSeatId(), "Seat ID should be generated");
        assertEquals("A1", saved.getSeatNumber());

        Optional<Seat> found = seatRepository.findById(saved.getSeatId());
        assertTrue(found.isPresent(), "Saved seat should be found");
        assertEquals("A1", found.get().getSeatNumber());
        assertEquals("A", found.get().getSeatRow());
        assertEquals(show.getShowId(), found.get().getShow().getShowId());
    }

    @Test
    void testFindAllSeats() {
        Show show = persistShow();

        Seat s1 = buildSeat("A2", "A", "VIP", 220.0, false, show);
        Seat s2 = buildSeat("B1", "B", "REGULAR", 180.0, true, show);
        seatRepository.save(s1);
        seatRepository.save(s2);

        List<Seat> seats = seatRepository.findAll();
        assertFalse(seats.isEmpty(), "Seats should not be empty");
        assertTrue(seats.size() >= 2, "At least two seats should be present");
    }

    @Test
    void testDeleteSeat() {
        Show show = persistShow();

        Seat seat = seatRepository.save(buildSeat("B1", "B", "REGULAR", 180.0, true, show));
        Long id = seat.getSeatId();
        assertNotNull(id);

        seatRepository.deleteById(id);
        Optional<Seat> deleted = seatRepository.findById(id);
        assertFalse(deleted.isPresent(), "Seat should be deleted");
    }
}
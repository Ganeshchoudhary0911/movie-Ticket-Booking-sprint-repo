package com.cg.service;

import com.cg.dto.SeatDto;
import com.cg.entity.Seat;
import com.cg.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SeatService implements ISeatService {

    @Autowired
    private SeatRepository seatRepository;
    
    // ============================================================
    // GET seats by showId (for loading seat-selection page)
    // ============================================================
    @Override
    public List<SeatDto> getSeatsByShow(Long showId) {
        return seatRepository.findByShowShowId(showId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ============================================================
    // GET seat by ID (simple)
    // ============================================================
    @Override
    public SeatDto getSeatById(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));
        return toDto(seat);
    }

    // ============================================================
    // MULTI-SEAT BOOKING (BookMyShow-style)
    // ============================================================
    public Set<Seat> bookSeats(List<Long> seatIds) {

        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("Seat IDs cannot be empty");
        }

        // Fetch all seats
        List<Seat> seats = seatRepository.findBySeatIdIn(seatIds);

        if (seats.size() != seatIds.size()) {
            throw new RuntimeException("Some seats do not exist");
        }

        // Validate none are already booked
        for (Seat s : seats) {
            if (s.isBooked()) {
                throw new RuntimeException("Seat already booked: "
                        + s.getSeatRow() + s.getSeatNumber());
            }
        }

        // Mark seats booked
        seats.forEach(s -> s.setBooked(true));

        // Save all
        seatRepository.saveAll(seats);

        return new HashSet<>(seats);
    }

    // ============================================================
    // Used by UI to disable booked seats
    // ============================================================
    public List<String> getBookedSeats(Long showId) {
        return seatRepository.findBookedSeatsByShowId(showId)
                .stream()
                .map(s -> s.getSeatRow() + s.getSeatNumber())
                .collect(Collectors.toList());
    }

    // ============================================================
    // Convert Entity -> DTO
    // ============================================================
    private SeatDto toDto(Seat seat) {
        SeatDto dto = new SeatDto();
        dto.setSeatId(seat.getSeatId());
        dto.setSeatNumber(seat.getSeatNumber());
        dto.setSeatRow(seat.getSeatRow());
        dto.setSeatType(seat.getSeatType());
        dto.setSeatPrice(seat.getSeatPrice());
        dto.setBooked(seat.isBooked());
        dto.setShowId(seat.getShow() != null ? seat.getShow().getShowId() : null);
        return dto;
    }
}
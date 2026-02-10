package com.cg.service;

import com.cg.dto.SeatDto;
import java.util.List;

public interface ISeatService {

    // Fetch all seats for a show (use showId instead of Show entity)
    List<SeatDto> getSeatsByShow(Long showId);

    // Get a single seat by its ID
    SeatDto getSeatById(Long seatId);

    // Mark a seat as booked and return updated DTO
//    SeatDto markSeatAsBooked(Long seatId);
}
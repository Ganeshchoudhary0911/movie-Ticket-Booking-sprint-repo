package com.cg.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.entity.Seat;
import com.cg.repository.SeatRepository;

@Service
public class SeatService {

	@Autowired
	private SeatRepository seatRepository;
	
	//Add seat
	public Seat addSeat(Seat seat) {
		seat.setBooked(false);         //default value
		return seatRepository.save(seat);
	}
	
	//Get all seats for show
	public List<Seat> getSeatByShow(Long showId){
		return seatRepository.findByShowShowId(showId);
	}
	
	// Get available seats for a show
    public List<Seat> getAvailableSeats(Long showId) {
        return seatRepository.findByShowShowIdAndIsBookedFalse(showId);
    }
 
    // Book a seat
    public Seat bookSeat(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));
 
        if (seat.isBooked()) {
            throw new RuntimeException("Seat already booked");
        }
 
        seat.setBooked(true);
        return seatRepository.save(seat);
    }
}
package com.cg.service;

import com.cg.dto.SeatDto;
import com.cg.entity.Seat;
import com.cg.entity.Show;
import com.cg.repository.BookingRepository;
import com.cg.repository.SeatRepository;
import com.cg.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatService implements ISeatService {

	@Autowired
	private SeatRepository seatRepository;

	@Autowired
	private ShowRepository showRepository;
	
	@Autowired
	BookingRepository bookingRepository;

	// ========== READ (by show) ==========
	// Changed parameter to showId to work with DTOs and avoid passing entities
	// around
	@Override
	public List<SeatDto> getSeatsByShow(Long showId) {
		Show show = showRepository.findById(showId).orElseThrow(() -> new RuntimeException("Show not found"));

		return seatRepository.findByShow(show).stream().map(this::toDto).collect(Collectors.toList());
	}

	// ========== READ (by id) ==========
	@Override
	public SeatDto getSeatById(Long seatId) {
		Seat seat = seatRepository.findById(seatId).orElseThrow(() -> new RuntimeException("Seat not found"));
		return toDto(seat);
	}

	// ========== UPDATE (mark booked) ==========
	@Override
	public SeatDto markSeatAsBooked(Long seatId) {
		Seat seat = seatRepository.findById(seatId).orElseThrow(() -> new RuntimeException("Seat not found"));
		seat.setBooked(true);
		Seat saved = seatRepository.save(seat);
		return toDto(saved);
	}

	// ===== Manual mapping (Entity -> DTO) =====
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

	public List<String> getBookedSeats(Long showId) {
		return bookingRepository.findBookedSeatsByShowId(showId);
	}

}
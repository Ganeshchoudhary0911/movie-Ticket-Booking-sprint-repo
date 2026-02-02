package com.cg.controller;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
 
import com.cg.entity.Seat;
import com.cg.service.SeatService;
 
@Controller
@RequestMapping("/api/seats")
public class SeatController {
 
	@Autowired
    private final SeatService seatService;
 
    // ✅ Constructor injection
    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }
 
    // 1️⃣ Add a seat
    @PostMapping("/add")
    public Seat addSeat(@RequestBody Seat seat) {
        return seatService.addSeat(seat);
    }
 
    // 2️⃣ Get all seats for a show
    @GetMapping("/show/{showId}")
    public List<Seat> getSeatsByShow(@PathVariable Long showId) {
        return seatService.getSeatByShow(showId);
    }
 
    // 3️⃣ Get available seats
    @GetMapping("/available/{showId}")
    public List<Seat> getAvailableSeats(@PathVariable Long showId) {
        return seatService.getAvailableSeats(showId);
    }
 
    // 4️⃣ Book a seat
    @PutMapping("/book/{seatId}")
    public Seat bookSeat(@PathVariable Long seatId) {
        return seatService.bookSeat(seatId);
    }
}
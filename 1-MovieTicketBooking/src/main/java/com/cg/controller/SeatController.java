package com.cg.controller;
 
import com.cg.entity.Seat;
import com.cg.entity.Show;
import com.cg.service.SeatService;
import com.cg.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
 
@Controller
public class SeatController {
 
    @Autowired
    private SeatService seatService;
 
    @Autowired
    private ShowService showService;
 
    // User: open seat selection page
    @GetMapping("/seats/{showId}")
    public String showSeats(@PathVariable Long showId, Model model) {
 
        Show show = showService.getShowById(showId);
 
        model.addAttribute("show", show);
        model.addAttribute("seats", seatService.getSeatsByShow(show));
 
        return "seat-selection";
    }
 
    // User: book a seat
    @PostMapping("/seats/book/{seatId}")
    public String bookSeat(@PathVariable Long seatId) {
 
        seatService.markSeatAsBooked(seatId);
 
        return "redirect:/booking/confirm/" + seatId;
    }
}
 
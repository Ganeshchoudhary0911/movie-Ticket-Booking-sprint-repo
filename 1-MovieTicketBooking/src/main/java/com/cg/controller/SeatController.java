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
 
        model.addAttribute("movie", show.getMovie());
        model.addAttribute("theatre", show.getTheatre());
        model.addAttribute("showDate", show.getShowDate());
        model.addAttribute("showTime", show.getShowTime());
 
        return "seat-selection";
    }
 
    // User: book a seat
    @PostMapping("/seats/book/{seatId}")
    public String bookSeat(@PathVariable Long seatId) {
 
        seatService.markSeatAsBooked(seatId);
 
        return "redirect:/booking/confirm/" + seatId;
    }
    
//    @GetMapping("/seats/{id}")
//    public String seatsPage(@PathVariable Long id, Model model) {
//
//        Show show = showService.getShowById(id);
//
//        model.addAttribute("movie", show.getMovie());
//        model.addAttribute("theatre", show.getTheatre());
//        model.addAttribute("showDate", show.getShowDate());
//        model.addAttribute("showTime", show.getShowTime());
//
//        return "seat-selection";
//    }
}
 
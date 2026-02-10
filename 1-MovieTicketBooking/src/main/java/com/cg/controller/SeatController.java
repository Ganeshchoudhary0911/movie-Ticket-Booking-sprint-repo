package com.cg.controller;

import com.cg.dto.ShowDto;
import com.cg.service.SeatService;
import com.cg.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SeatController {

    @Autowired
    private SeatService seatService;

    @Autowired
    private ShowService showService;

    // ============================================================
    // SHOW SEAT SELECTION PAGE
    // ============================================================
    @GetMapping("/seats/{showId}")
    public String showSeats(@PathVariable Long showId, Model model) {

        // 1) Get show details
        ShowDto show = showService.getShowById(showId);
        if (show == null) {
            model.addAttribute("error", "Show not found");
            return "error";
        }

        // 2) Get already-booked seats like "A1", "B3"
        List<String> bookedSeats = seatService.getBookedSeats(showId);

        // 3) Add data to model for Thymeleaf page
        model.addAttribute("showId", showId);
        model.addAttribute("movieName", show.getMovieName());
        model.addAttribute("theatreName", show.getTheatreName());
        model.addAttribute("showDate", show.getShowDate());
        model.addAttribute("showTime", show.getShowTime());

        model.addAttribute("bookedSeats", bookedSeats);

        return "seat-selection";
    }
}
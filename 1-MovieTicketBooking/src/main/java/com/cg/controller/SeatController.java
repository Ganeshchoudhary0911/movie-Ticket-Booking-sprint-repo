package com.cg.controller;

import com.cg.dto.MovieDto;
import com.cg.dto.ShowDto;
import com.cg.dto.TheatreDto;
import com.cg.service.SeatService;
import com.cg.service.ShowService;
import com.cg.service.MovieService;
import com.cg.service.TheatreService;
import com.cg.entity.Movie;
import com.cg.entity.Theatre;
import com.cg.repository.BookingRepository;

import java.util.List;

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

    // Added to retrieve Movie/Theatre entities for the view, since ShowDto only has IDs
    @Autowired
    private MovieService movieService;

    @Autowired
    private TheatreService theatreService;
    
    @Autowired
    BookingRepository bookingRepository;

    // User: open seat selection page
    @GetMapping("/seats/{showId}")
    public String showSeats(@PathVariable Long showId, Model model) {

        ShowDto show = showService.getShowById(showId);

        List<String> bookedSeats = bookingRepository.findBookedSeatsByShowId(showId);

        model.addAttribute("showId", showId);
        model.addAttribute("movieName", show.getMovieName());
        model.addAttribute("theatreName", show.getTheatreName());
        model.addAttribute("showDate", show.getShowDate());
        model.addAttribute("showTime", show.getShowTime());

        model.addAttribute("bookedSeats", bookedSeats);
        model.addAttribute("showId", showId);

        return "seat-selection";
    }

    // User: book a seat
    @PostMapping("/seats/book/{seatId}")
    public String bookSeat(@PathVariable Long seatId) {

        // Service now returns DTO; we don’t need the return value here
        seatService.markSeatAsBooked(seatId);

        return "redirect:/booking/confirm/" + seatId;
    }

//    @GetMapping("/seats/{id}")
//    public String seatsPage(@PathVariable Long id, Model model) {
//
//        ShowDto show = showService.getShowById(id);
//        Movie movie = movieService.getMovieById(show.getMovieId());
//        Theatre theatre = theatreService.getTheatreById(show.getTheatreId());
//
//        model.addAttribute("movie", movie);
//        model.addAttribute("theatre", theatre);
//        model.addAttribute("showDate", show.getShowDate());
//        model.addAttribute("showTime", show.getShowTime());
//
//        return "seat-selection";
//    }
}
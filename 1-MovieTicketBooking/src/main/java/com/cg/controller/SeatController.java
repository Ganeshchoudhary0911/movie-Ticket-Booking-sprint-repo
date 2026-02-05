package com.cg.controller;
 
import com.cg.entity.Seat;
import com.cg.entity.Show;
import com.cg.service.ISeatService;
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
    private ShowService showService;
 
    @GetMapping("/seats/{showId}")
    public String seatPage(@PathVariable Long showId, Model model) {
 
        Show show = showService.getShowById(showId);
 
        model.addAttribute("movieName", show.getMovie().getMovieName());
        model.addAttribute("theatreName", show.getTheatre().getTheatreName());
        model.addAttribute("showDate", show.getShowDate());
        model.addAttribute("showTime", show.getShowTime());
 
        return "seat-selection";
    }
 
    // 🔥 THIS METHOD IS IMPORTANT
    @GetMapping("/payment")
    public String paymentPage(
            @RequestParam int seats,
            @RequestParam int amount,
            Model model) {
 
        model.addAttribute("seatCount", seats);
        model.addAttribute("totalAmount", amount);
 
        return "payment";
    }
}
 
 
//@Controller
//public class SeatController {
// 
//    @Autowired
//    private ShowService showService;
// 
//    @GetMapping("/seats/{showId}")
//    public String seatPage(@PathVariable Long showId, Model model) {
// 
//        System.out.println("Seat page hit with showId = " + showId);
// 
//        Show show = showService.getShowById(showId);
// 
//        if (show == null) {
//            System.out.println("Show is NULL");
//            return "seat-selection";
//        }
// 
//        System.out.println("Movie = " + show.getMovie().getMovieName());
//        System.out.println("Theatre = " + show.getTheatre().getTheatreName());
//        System.out.println("Date = " + show.getShowDate());
//        System.out.println("Time = " + show.getShowTime());
// 
//        model.addAttribute("movieName", show.getMovie().getMovieName());
//        model.addAttribute("theatreName", show.getTheatre().getTheatreName());
//        model.addAttribute("showDate", show.getShowDate());
//        model.addAttribute("showTime", show.getShowTime());
// 
//        return "seat-selection";
//    }
//
//
//    // Payment page (NO JS)
//    @PostMapping("/payment")
//    public String paymentPage(
//            @RequestParam int seats,
//            @RequestParam int amount,
//            Model model) {
//     
//        model.addAttribute("seats", seats);
//        model.addAttribute("totalAmount", amount);
//     
//        return "payment";
//    }
// // PaymentController.java
//    @Controller
//    public class PaymentController {
//
//        @GetMapping("/payment")
//        public String showPayment(@RequestParam int seats,
//                                  @RequestParam int amount,
//                                  Model model) {
//            model.addAttribute("seatCount", seats);
//            model.addAttribute("totalAmount", amount);
//            // Add any other data you need
//            return "payment"; // payment.html
//        }
//    }
//     
//}
// 



//package com.cg.controller;
// 
//import com.cg.entity.Seat;
//import com.cg.entity.Show;
//import com.cg.service.SeatService;
//import com.cg.service.ShowService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
// 
//@Controller
//public class SeatController {
// 
//    @Autowired
//    private SeatService seatService;
// 
//    @Autowired
//    private ShowService showService;
// 
//    // User: open seat selection page
//    @GetMapping("/seats/{showId}")
//    public String showSeats(@PathVariable Long showId, Model model) {
// 
//        Show show = showService.getShowById(showId);
// 
//        model.addAttribute("show", show);
//        model.addAttribute("seats", seatService.getSeatsByShow(show));
// 
//        return "seat-selection";
//    }
// 
//    // User: book a seat
//    @PostMapping("/seats/book/{seatId}")
//    public String bookSeat(@PathVariable Long seatId) {
// 
//        seatService.markSeatAsBooked(seatId);
// 
//        return "redirect:/booking/confirm/" + seatId;
//    }
//}
// 
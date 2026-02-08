package com.cg.service;
 
import com.cg.entity.Seat;
import com.cg.entity.Show;
 
import java.util.List;
 
public interface ISeatService {
 
    List<Seat> getSeatsByShow(Show show);
 
    Seat getSeatById(Long seatId);
 
    Seat markSeatAsBooked(Long seatId);
}
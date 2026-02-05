package com.cg.service;
 
import com.cg.entity.Seat;
import com.cg.entity.Show;
import com.cg.repository.SeatRepository;
import com.cg.service.ISeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import java.util.List;
 
@Service
public class SeatService implements ISeatService {
 
    @Autowired
    private SeatRepository seatRepository;
 
    @Override
    public List<Seat> getSeatsByShow(Show show) {
        return seatRepository.findByShow(show);
    }
 
    @Override
    public Seat getSeatById(Long seatId) {
        return seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));
    }
 
    @Override
    public List<Seat> getSeatsByIds(List<Long> seatIds) {
        return seatRepository.findAllById(seatIds);
    }
 
    @Override
    public Seat markSeatAsBooked(Long seatId) {
        Seat seat = getSeatById(seatId);
        seat.setBooked(true);
        return seatRepository.save(seat);
    }
}
 
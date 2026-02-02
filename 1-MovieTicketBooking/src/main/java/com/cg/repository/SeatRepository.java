package com.cg.repository;
 
import com.cg.entity.Seat;
import com.cg.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
 
public interface SeatRepository extends JpaRepository<Seat, Long> {
 
    List<Seat> findByShow(Show show);
}
package com.cg.repository;

import com.cg.entity.Seat;
import com.cg.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    // Existing method
    List<Seat> findByShow(Show show);

    // ⭐ NEW 1 — Fetch all seats for show using showId only (simplifies controller)
    List<Seat> findByShowShowId(Long showId);

    // ⭐ NEW 2 — Fetch multiple seats by seatId list (for booking validation)
    List<Seat> findBySeatIdIn(List<Long> seatIds);

    // ⭐ NEW 3 — Fetch only booked seats for a show (optional but useful)
    @Query("SELECT s FROM Seat s WHERE s.show.showId = :showId AND s.isBooked = true")
    List<Seat> findBookedSeatsByShowId(Long showId);
}
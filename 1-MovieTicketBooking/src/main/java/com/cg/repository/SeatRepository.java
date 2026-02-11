package com.cg.repository;

import com.cg.entity.Seat;
import com.cg.entity.Show;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

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

	List<Seat> findByShow_ShowIdOrderBySeatRowAscSeatNumberAsc(Long showId);

	boolean existsByShow_ShowId(Long showId);
	
	Optional<Seat> findByShow_ShowIdAndSeatRowAndSeatNumber(Long showId, String seatRow, String seatNumber);

	@Modifying
	@Transactional
	@Query("""
			    update Seat s
			       set s.isBooked = true
			     where s.show.showId = :showId
			       and s.seatId in :seatIds
			       and s.isBooked = false
			""")
	int markAvailableSeatsAsBooked(@Param("showId") Long showId, @Param("seatIds") List<Long> seatIds);
	

	@Modifying
    @Transactional
    @Query("delete from Seat s where s.show.showId = :showId")
    void deleteByShowId(@Param("showId") Long showId);

    @Modifying
    @Transactional
    @Query("update Seat s set s.seatPrice = :price where s.show.showId = :showId")
    int updateSeatPricesForShow(@Param("showId") Long showId, @Param("price") double newPrice);


}
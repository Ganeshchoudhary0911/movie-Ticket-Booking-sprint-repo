package com.cg.repository;
	 
import com.cg.entity.Seat;

	import com.cg.entity.Show;
	import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
	public interface SeatRepository extends JpaRepository<Seat, Long> {
	 
	//get all seats for a show
	    List<Seat> findByShowShowId(Long showId);
	
	//get only available for a show    
	    List<Seat> findByShowShowIdAndIsBookedFalse(Long showId);
}

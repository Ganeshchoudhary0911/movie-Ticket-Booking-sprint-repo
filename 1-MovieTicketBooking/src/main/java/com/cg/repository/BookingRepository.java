package com.cg.repository;

import java.util.List;
import java.util.Optional;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cg.entity.Booking;
import com.cg.entity.User;

public interface BookingRepository extends JpaRepository<Booking, Long> {

	List<Booking> findByUser(User user);

	// Correct query: Join from Booking to Seat and filter on Show + payment status
	@Query("""
			select s.seatNumber
			from Booking b
			join b.seats s
			where b.show.showId = :showId
			  and b.paymentStatus = 'PAID'
			""")
	List<String> findBookedSeatsByShowId(Long showId);

	@EntityGraph(attributePaths = { "show", "user", "show.movie", "show.theatre" })
	Optional<Booking> findById(Long bookingId);

}
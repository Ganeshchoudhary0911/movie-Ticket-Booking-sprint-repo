package com.cg.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.entity.Booking;
import com.cg.entity.User;

public interface BookingRepository extends JpaRepository<Booking, Long> {
	
	List<Booking> findByUser(User user);

	List<Booking> findByUser(Optional<User> user);
}
package com.cg.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	 
	    User findByEmail(String email);
		User findByUsername(String username);
		
		
		
	}
	 


package com.cg.security;

import com.cg.entity.User;
import com.cg.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
    UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

	    User user = userRepo.findByUsername(username);

	    if (user == null) {
	        throw new UsernameNotFoundException("User not found: " + username);
	    }

	    return org.springframework.security.core.userdetails.User
	            .withUsername(user.getUsername())
	            .password(user.getPassword())
	            .authorities("ROLE_" + user.getRole().name())
	            .build();
	}
}
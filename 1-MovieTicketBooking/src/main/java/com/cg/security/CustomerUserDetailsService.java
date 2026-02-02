package com.cg.security;


	import com.cg.entity.User;
	import com.cg.repository.UserRepository;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.security.core.userdetails.*;
	import org.springframework.stereotype.Service;
	 
	import java.util.Collections;
	 
	@Service
	public class CustomUserDetailsService implements UserDetailsService {
	 
	    @Autowired
	    private UserRepository userRepo;
	 
	    @Override
	    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	 
	        User user = userRepo.findByEmail(email);
	 
	        if (user == null) {
	            throw new UsernameNotFoundException("User not found");
	        }
	 
	        return new org.springframework.security.core.userdetails.User(
	                user.getEmail(),
	                user.getPassword(),
	                Collections.singleton(() -> "ROLE_" + user.getRole().name())
	        );
	    }
	}


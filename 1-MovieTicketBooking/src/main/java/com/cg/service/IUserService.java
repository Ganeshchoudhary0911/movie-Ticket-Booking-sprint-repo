package com.cg.service;

	 
	import com.cg.entity.User;
	 
	public interface IUserService {
	 
	    User saveUser(User user);
	 
	    User findByEmail(String email);
	}


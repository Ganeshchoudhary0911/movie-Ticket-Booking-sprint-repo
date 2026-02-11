package com.cg.service;
 
import java.util.Optional;
 
import com.cg.dto.SignupDto;
import com.cg.dto.UserDto;
 
public interface IUserService {
	UserDto saveUser(UserDto user); // create/update using safe DTO
 
	Optional<UserDto> findByEmail(String email); // returns safe DTO
 
	Optional<UserDto> findByUsername(String username);
 
	boolean existsByUsername(String username);
 
	boolean existsByEmail(String email);
 
	UserDto createUser(SignupDto signupDto); // handles password encoding, defaults, mapping
 
}
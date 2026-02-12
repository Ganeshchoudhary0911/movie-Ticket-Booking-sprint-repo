package com.cg.service;
 
import java.util.Optional;
 
import com.cg.dto.SignupDto;
import com.cg.dto.UserDto;
import com.cg.entity.User;
 
public interface IUserService {
	
	UserDto saveUser(UserDto user); // create/update using safe DTO
	Optional<UserDto> findByEmail(String email); // returns safe DTO
	Optional<UserDto> findByUsername(String username);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	UserDto createUser(SignupDto signupDto); // handles password encoding, defaults, mapping
 
	
	 // New entity methods for ProfileController
    Optional<User> findEntityByUsername(String username);
    Optional<User> findEntityByEmail(String email);
    Optional<User> findEntityByUsernameOrEmail(String login);
    User saveEntity(User user);
}
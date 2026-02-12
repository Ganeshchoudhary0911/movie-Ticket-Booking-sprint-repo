package com.cg.dto;

import com.cg.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserDto {

	private Long userId;

	@NotBlank(message = "Username is required")
	@Size(min = 3, max = 50, message = "Username must be 3–50 characters")
	@Pattern(regexp = "^[A-Za-z0-9_.-]+$", message = "Username can contain letters, numbers, dot, underscore, hyphen")
	private String username;

	@NotBlank(message = "Email is required")
	@Email(message = "Enter a valid email address")
	@Size(max = 50, message = "Email must be at most 50 characters")
	private String email;

	private Role role; // ADMIN or USER

	@NotBlank(message = "Phone number is required")
	@Pattern(regexp = "^(\\+91[- ]?)?[6-9]\\d{9}$", message = "Enter a valid Indian mobile number")
	private String phoneNumber;
	private boolean enabled;

	public UserDto() {
	}

	public UserDto(Long userId, String username, String email, Role role, String phoneNumber, boolean enabled) {
		super();
		this.userId = userId;
		this.username = username;
		this.email = email;
		this.role = role;
		this.phoneNumber = phoneNumber;
		this.enabled = enabled;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}

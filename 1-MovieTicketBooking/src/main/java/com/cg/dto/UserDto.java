package com.cg.dto;

import com.cg.entity.Role;

public class UserDto {

	private Long userId;
	private String username;
	private String email;
	private Role role; // ADMIN or USER
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

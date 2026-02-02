package com.cg.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


	@Entity
	@Table(name = "users")
	public class User {
	 
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long userId;
	 
	    private String username;
	    private String email;
	    private String password;
	 
	   // @Enumerated(EnumType.STRING)
	    private String role;   // ADMIN or USER
	 
	   
	    @OneToMany(mappedBy = "user")
	    private List<Booking> bookings;


		public User() {
			super();
			// TODO Auto-generated constructor stub
		}


		public User(Long userId, String username, String email, String password, String role, List<Booking> bookings) {
			super();
			this.userId = userId;
			this.username = username;
			this.email = email;
			this.password = password;
			this.role = role;
			this.bookings = bookings;
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


		public String getPassword() {
			return password;
		}


		public void setPassword(String password) {
			this.password = password;
		}


		public String getRole() {
			return role;
		}


		public void setRole(String role) {
			this.role = role;
		}


		public List<Booking> getBookings() {
			return bookings;
		}


		public void setBookings(List<Booking> bookings) {
			this.bookings = bookings;
		}
	    
	    
}

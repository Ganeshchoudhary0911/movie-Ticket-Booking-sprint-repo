package com.cg.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class TheatreDto {

	private Long theatreId;

	@NotBlank(message = "Theatre name is required")
	@Size(min = 2, max = 100)
	private String theatreName;

	@NotBlank(message = "Location is required")
	@Size(min = 2, max = 150)
	private String location;

	@NotBlank(message = "City is required")
	@Size(min = 2, max = 100)
	private String city;

	@Min(value = 50, message = "Total seats must be at least 50")
	@Max(value = 1000, message = "Total seats cannot exceed 1000")
	private Integer totalSeats;

	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Contact number must be a valid 10-digit Indian mobile number")
	private String contactNumber;

	public TheatreDto() {
	}

	public TheatreDto(Long theatreId, String theatreName, String location, String city, Integer totalSeats,
			String contactNumber) {
		super();
		this.theatreId = theatreId;
		this.theatreName = theatreName;
		this.location = location;
		this.city = city;
		this.totalSeats = totalSeats;
		this.contactNumber = contactNumber;
	}

	public Long getTheatreId() {
		return theatreId;
	}

	public void setTheatreId(Long theatreId) {
		this.theatreId = theatreId;
	}

	public String getTheatreName() {
		return theatreName;
	}

	public void setTheatreName(String theatreName) {
		this.theatreName = theatreName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(Integer totalSeats) {
		this.totalSeats = totalSeats;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

}

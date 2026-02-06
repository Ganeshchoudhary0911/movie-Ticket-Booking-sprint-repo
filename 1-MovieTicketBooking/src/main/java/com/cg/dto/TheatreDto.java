package com.cg.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class TheatreDto {

	private Long theatreId;

	@NotBlank
	private String theatreName;

	@NotBlank
	private String location;

	@NotBlank
	private String city;
	
	@Min(1)
	private int totalSeats;
	private String contactNumber;

	public TheatreDto() {
	}

	public TheatreDto(Long theatreId, String theatreName, String location, String city, int totalSeats,
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

	public int getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

}

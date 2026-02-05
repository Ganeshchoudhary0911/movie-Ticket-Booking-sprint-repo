package com.cg.entity;

import jakarta.persistence.*;
import java.util.List;
 
@Entity
@Table
public class Theatre {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long theatreId;
 
    @Column(nullable = false, length = 100)
    private String theatreName;
    private String location;
    private String city;
    private int totalSeats;
    private String contactNumber;
 
    @OneToMany(mappedBy = "theatre")
    private List<Show> shows;

	public Theatre() {

	}

	public Theatre(Long theatreId, String theatreName, String location, String city, int totalSeats,
			String contactNumber, List<Show> shows) {
		super();
		this.theatreId = theatreId;
		this.theatreName = theatreName;
		this.location = location;
		this.city = city;
		this.totalSeats = totalSeats;
		this.contactNumber = contactNumber;
		this.shows = shows;
	}

	public Theatre(String theatreName, String location) {  // Constructe for TestCases
		super();
		this.theatreName = theatreName;
		this.location = location;
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

	public List<Show> getShows() {
		return shows;
	}

	public void setShows(List<Show> shows) {
		this.shows = shows;
	}
 
    
}
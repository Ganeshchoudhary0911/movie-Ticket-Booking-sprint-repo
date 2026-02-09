package com.cg.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class BookingDto {

    private Long bookingId;
    private LocalDate bookingDate;
    private double totalAmount;
    private String paymentStatus;
    private String bookingStatus;

    // Reference by ID
    private Long userId;
    private Long showId;

    // Presentational fields you already use in templates
    private String userUsername;

    // --- NEW: Show/Theatre display fields ---
    private String movieName;
    private String theatreName;
    private LocalDate showDate;
    private LocalTime showTime;
    private Double showPrice; // optional

    public BookingDto() {}

    public BookingDto(Long bookingId, LocalDate bookingDate, double totalAmount, String paymentStatus,
                      String bookingStatus, Long userId, Long showId, String userUsername) {
        this.bookingId = bookingId;
        this.bookingDate = bookingDate;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.bookingStatus = bookingStatus;
        this.userId = userId;
        this.showId = showId;
        this.userUsername = userUsername;
    }

    // getters/setters
    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getShowId() { return showId; }
    public void setShowId(Long showId) { this.showId = showId; }
    public String getUserUsername() { return userUsername; }
    public void setUserUsername(String userUsername) { this.userUsername = userUsername; }

    public String getMovieName() { return movieName; }
    public void setMovieName(String movieName) { this.movieName = movieName; }
    public String getTheatreName() { return theatreName; }
    public void setTheatreName(String theatreName) { this.theatreName = theatreName; }
    public LocalDate getShowDate() { return showDate; }
    public void setShowDate(LocalDate showDate) { this.showDate = showDate; }
    public LocalTime getShowTime() { return showTime; }
    public void setShowTime(LocalTime showTime) { this.showTime = showTime; }
    public Double getShowPrice() { return showPrice; }
    public void setShowPrice(Double showPrice) { this.showPrice = showPrice; }
}
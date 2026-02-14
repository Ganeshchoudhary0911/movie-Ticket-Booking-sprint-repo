package com.cg.exception;

// Custom exception for movie-specific search failures
public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException(String message) {
        super(message);
    }
}

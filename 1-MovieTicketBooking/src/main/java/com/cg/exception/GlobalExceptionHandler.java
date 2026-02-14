package com.cg.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MovieNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleMovieNotFound(MovieNotFoundException ex, Model model) {
        log.warn("Movie search failed: {}", ex.getMessage());
        model.addAttribute("error", Map.of(
                "message", "Movie Not Found",
                "details", ex.getMessage()
        ));
        return "error/error-page";
    }

    // Optional: your generic resource exception
    @ExceptionHandler(ResourceNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFound(ResourceNotFound ex, Model model) {
        log.warn("Resource not found: {}", ex.getMessage());
        model.addAttribute("error", Map.of(
                "message", "404 - Not Found",
                "details", ex.getMessage()
        ));
        return "error/error-page";
    }

    // Boot 3.x unmapped URL handler
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoResourceFound(NoResourceFoundException ex, Model model) {
        log.warn("404 - Page Not Found: {}", ex.getResourcePath());
        model.addAttribute("error", Map.of(
                "message", "404 - Page Not Found",
                "details", "The URL you requested does not exist: " + ex.getResourcePath()
        ));
        return "error/error-page";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationExceptions(MethodArgumentNotValidException ex, Model model) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.toList());

        model.addAttribute("error", Map.of(
                "message", "Validation Failed",
                "details", "Please correct the highlighted fields below."
        ));
        model.addAttribute("errors", errors);
        return "error/error-page";
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleRuntime(RuntimeException ex, Model model) {
        log.error("Unexpected error", ex);
        model.addAttribute("error", Map.of(
                "message", "500 - Internal Server Error",
                "details", "Something went wrong on our end. " + ex.getMessage()
        ));
        return "error/error-page";
    }
}
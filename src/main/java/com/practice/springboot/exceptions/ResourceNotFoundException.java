package com.practice.springboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException forInvalidId(MethodArgumentTypeMismatchException ex) {
        String message = String.format("Invalid ID format: %s. Please provide a valid integer ID.",
                ex.getValue());
        return new ResourceNotFoundException(message);
    }
}

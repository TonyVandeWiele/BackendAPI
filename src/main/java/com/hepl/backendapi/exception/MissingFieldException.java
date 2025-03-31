package com.hepl.backendapi.exception;

public class MissingFieldException extends RuntimeException {
    public MissingFieldException(String fieldName) {
        super("Missing required field: " + fieldName);
    }
}

package com.hepl.backendapi.exception;

public class AlreadyAssignedException extends RuntimeException {
    public AlreadyAssignedException(String message) {
        super(message);
    }
}

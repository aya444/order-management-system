package com.aya.user_service.exception;

public class CannotCreateAdminException extends RuntimeException {
    public CannotCreateAdminException(String message) {
        super(message);
    }
}

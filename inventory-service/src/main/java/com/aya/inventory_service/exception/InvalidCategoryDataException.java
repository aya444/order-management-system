package com.aya.inventory_service.exception;

public class InvalidCategoryDataException extends RuntimeException {
    public InvalidCategoryDataException(String message) {
        super(message);
    }
}

package com.aya.order_service.exception;

public class NoProductFoundException extends RuntimeException {
    public NoProductFoundException(String message) {
        super(message);
    }
}

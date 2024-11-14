package com.aya.order_service.exception;

public class NoOrderFoundException extends RuntimeException {
  public NoOrderFoundException(String message) {
    super(message);
  }
}

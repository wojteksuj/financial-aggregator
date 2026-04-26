package com.aggregator.alert.exception;

public class AlertNotFoundException extends RuntimeException {
    public AlertNotFoundException(String id) {
        super("alert not found with id: " + id);
    }
}

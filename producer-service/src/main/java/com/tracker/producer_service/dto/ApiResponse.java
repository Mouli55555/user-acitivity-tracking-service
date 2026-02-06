package com.tracker.producer_service.controller;

public class ApiResponse {

    private String message;

    public ApiResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

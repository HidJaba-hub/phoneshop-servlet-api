package com.es.phoneshop.exception;

public class OrderNotFoundException extends RuntimeException {

    private String id;

    public OrderNotFoundException(String id, String message) {
        super(message);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}

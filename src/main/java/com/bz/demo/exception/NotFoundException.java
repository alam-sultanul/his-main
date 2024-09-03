package com.bz.demo.exception;

public class NotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1346661033386714510L;

    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }
}

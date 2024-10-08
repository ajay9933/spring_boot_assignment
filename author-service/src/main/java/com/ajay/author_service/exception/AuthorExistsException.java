package com.ajay.author_service.exception;

public class AuthorExistsException extends RuntimeException {
    public AuthorExistsException(String message) {
        super(message);
    }
}

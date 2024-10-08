package com.ajay.book_service.exceptions;

public class BookExistsException extends RuntimeException {
    public BookExistsException(String message) {
        super(message);
    }
}

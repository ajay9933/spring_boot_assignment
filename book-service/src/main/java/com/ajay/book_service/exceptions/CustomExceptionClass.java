package com.ajay.book_service.exceptions;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomExceptionClass {
    private int status;
    private String message;

    private String timestamp;
}


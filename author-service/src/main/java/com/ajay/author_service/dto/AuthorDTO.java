package com.ajay.author_service.dto;


import com.ajay.author_service.model.Book;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AuthorDTO {

    private int id;
    private String name;

    private List<Book> books = new ArrayList<>();

    public AuthorDTO(int id, String name,List<Book> books) {
        this.id = id;
        this.name = name;
        this.books=books;
    }
}

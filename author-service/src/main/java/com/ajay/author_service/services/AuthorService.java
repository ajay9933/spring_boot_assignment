package com.ajay.author_service.services;

import com.ajay.author_service.dto.AuthorDTO;
import com.ajay.author_service.model.Author;
import com.ajay.author_service.model.Book;

import java.util.List;

public interface AuthorService {
    List<AuthorDTO> findAll();
    AuthorDTO findById(int authorId);
    void save(Author author);
    void deleteAuthor(int authorId);
    Author updateAuthor(int authorId,Author author);
    List<Book> getBooksByAuthorId(int authorId);
}

package com.ajay.book_service.service;

import com.ajay.book_service.model.Book;

import java.util.List;

public interface BookService {
    List<Book> findAll();
    Book findById(int bookId);
    void save(Book book);
    void deleteBook(int bookId);
    Book updateBook(int bookId,Book book);
    List<Book> getBooksByAuthorId(int authorId);
}

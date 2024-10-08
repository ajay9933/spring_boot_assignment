package com.ajay.book_service.repository;

import com.ajay.book_service.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book,Integer> {
    List<Book> findBooksByAuthorId(int authorId);

    Optional<Book> findByName(String name);
}

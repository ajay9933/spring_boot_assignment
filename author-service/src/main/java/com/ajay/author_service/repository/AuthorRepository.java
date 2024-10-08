package com.ajay.author_service.repository;

import com.ajay.author_service.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author,Integer> {

    Optional<Author> findByName(String name);
}

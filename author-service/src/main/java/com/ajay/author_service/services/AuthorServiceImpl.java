package com.ajay.author_service.services;

import com.ajay.author_service.dto.AuthorDTO;
import com.ajay.author_service.exception.AuthorExistsException;
import com.ajay.author_service.exception.AuthorNotFoundException;
import com.ajay.author_service.model.Author;
import com.ajay.author_service.model.Book;
import com.ajay.author_service.repository.AuthorRepository;
import com.ajay.author_service.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {


    private final AuthorRepository authorRepository;
    private final RestTemplate restTemplate;

   @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, RestTemplate restTemplate) {
        this.authorRepository = authorRepository;
        this.restTemplate = restTemplate;
    }

    @Override

    public List<AuthorDTO> findAll() {
        List<Author> authors = authorRepository.findAll();
        return authors.stream()
                .map(author -> new AuthorDTO(author.getId(), author.getName(),author.getBooks()))
                .collect(Collectors.toList());
    }

    @Override
   public AuthorDTO findById(int authorId) {
        Optional<Author> author = authorRepository.findById(authorId);
        return author.map(a -> new AuthorDTO(a.getId(), a.getName(),a.getBooks()))
                .orElseThrow(()->new AuthorNotFoundException(Constants.AUTHOR_NOT_FOUND));
    }


    public void save(Author author) {
        Optional<Author> authorExists= authorRepository.findByName(author.getName());
        if(authorExists.isPresent()) {
            throw new AuthorExistsException(Constants.AUTHOR_EXISTS);
        }
        authorRepository.save(author);
    }

    @Override
    public void deleteAuthor(int authorId) {
        Author author=authorRepository.findById(authorId).orElseThrow(()->new AuthorNotFoundException(Constants.AUTHOR_NOT_FOUND));
        authorRepository.deleteById(authorId);
    }

    @Override
    public Author updateAuthor(int authorId, Author author) {
       Author oldAuthor = authorRepository.findById(authorId).orElseThrow(()->new AuthorNotFoundException(Constants.AUTHOR_NOT_FOUND));
       if(author.getName()!=null)
       {
           oldAuthor.setName(author.getName());
       }
       authorRepository.save(oldAuthor);
       return oldAuthor;
    }

    @Override
    public List<Book> getBooksByAuthorId(int authorId) {
        ResponseEntity<List<Book>> books = restTemplate.exchange(
                "http://localhost:8082/books/author/" + authorId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Book>>() {}
        );
        return books.getBody();
    }
}

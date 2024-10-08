package com.ajay.author_service.service;



import com.ajay.author_service.dto.AuthorDTO;
import com.ajay.author_service.exception.AuthorExistsException;
import com.ajay.author_service.exception.AuthorNotFoundException;
import com.ajay.author_service.model.Author;
import com.ajay.author_service.model.Book;
import com.ajay.author_service.repository.AuthorRepository;
import com.ajay.author_service.services.AuthorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class AuthorServiceTest {

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private RestTemplate restTemplate;

    private Author author;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        author = new Author(1, "Author Name", "password123");
    }

    @Test
    public void testFindAll() {
        Author author2 = new Author(2, "Another Author", "password456");
        List<Author> authors = Arrays.asList(author, author2);
        when(authorRepository.findAll()).thenReturn(authors);

        List<AuthorDTO> result = authorService.findAll();

        assertEquals(2, result.size());
        assertEquals("Author Name", result.get(0).getName());
        verify(authorRepository, times(1)).findAll();
    }

    @Test
    public void testFindById_Success() {
        when(authorRepository.findById(1)).thenReturn(Optional.of(author));

        AuthorDTO result = authorService.findById(1);

        assertNotNull(result);
        assertEquals("Author Name", result.getName());
        verify(authorRepository, times(1)).findById(1);
    }

    @Test
    public void testFindById_NotFound() {
        when(authorRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(AuthorNotFoundException.class, () -> authorService.findById(1));
        verify(authorRepository, times(1)).findById(1);
    }

    @Test
    public void testSave_NewAuthor() {
        when(authorRepository.findByName(author.getName())).thenReturn(Optional.empty());
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        authorService.save(author);

        verify(authorRepository, times(1)).save(author);
    }

    @Test
    public void testSave_ExistingAuthor() {
        when(authorRepository.findByName(author.getName())).thenReturn(Optional.of(author));

        assertThrows(AuthorExistsException.class, () -> authorService.save(author));
        verify(authorRepository, times(0)).save(any(Author.class));
    }

    @Test
    public void testDeleteAuthor_Success() {
        when(authorRepository.findById(1)).thenReturn(Optional.of(author));

        authorService.deleteAuthor(1);

        verify(authorRepository, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteAuthor_NotFound() {
        when(authorRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(AuthorNotFoundException.class, () -> authorService.deleteAuthor(1));
        verify(authorRepository, times(0)).deleteById(1);
    }

    @Test
    public void testUpdateAuthor_Success() {
        when(authorRepository.findById(1)).thenReturn(Optional.of(author));

        Author updatedAuthor = new Author();
        updatedAuthor.setName("Updated Author");

        Author result = authorService.updateAuthor(1, updatedAuthor);

        assertEquals("Updated Author", result.getName());
        verify(authorRepository, times(1)).findById(1);
    }

    @Test
    public void testUpdateAuthor_NotFound() {
        when(authorRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(AuthorNotFoundException.class, () -> authorService.updateAuthor(1, author));
        verify(authorRepository, times(1)).findById(1);
    }

    @Test
    public void testGetBooksByAuthorId() {
        Book book1 = new Book(1, 123456789, "Book One", 1);
        Book book2 = new Book(2, 987654321, "Book Two", 1);
        List<Book> books = Arrays.asList(book1, book2);

        ResponseEntity<List<Book>> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn(books);
        when(restTemplate.exchange(eq("http://localhost:8082/books/author/1"),
                eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);

        List<Book> result = authorService.getBooksByAuthorId(1);

        assertEquals(2, result.size());
        assertEquals("Book One", result.get(0).getName());
        verify(restTemplate, times(1)).exchange(eq("http://localhost:8082/books/author/1"),
                eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class));
    }


    @Test
    public void testUpdateAuthor_Success_NullName() {

        when(authorRepository.findById(1)).thenReturn(Optional.of(author));

        Author updatedAuthor = new Author();
        updatedAuthor.setName(null);


        Author result = authorService.updateAuthor(1, updatedAuthor);


        assertEquals("Author Name", result.getName());
        verify(authorRepository, times(1)).findById(1);
        verify(authorRepository, times(1)).save(author);
        verify(authorRepository, times(0)).save(updatedAuthor);
    }




}

package com.ajay.author_service.controller;

import com.ajay.author_service.Controller.AuthorController;
import com.ajay.author_service.dto.AuthorDTO;
import com.ajay.author_service.model.Author;
import com.ajay.author_service.model.Book;
import com.ajay.author_service.services.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class AuthorControllerTest {

    @InjectMocks
    private AuthorController authorController;

    @Mock
    private AuthorService authorService;

    private Author author;

    private AuthorDTO authorDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        author = new Author(1, "Author Name", "password123");
        List<Book> books = new ArrayList<>();
        books.add(new Book(1, 123456789, "Book One", 1));
        books.add(new Book(2, 987654321, "Book Two", 1));
        authorDTO = new AuthorDTO(1, "Author Name",books);
    }

    @Test
    public void testGetAllAuthors() {
        List<AuthorDTO> authors = Arrays.asList(authorDTO);
        when(authorService.findAll()).thenReturn(authors);

        List<AuthorDTO> result = authorController.getAuthors();

        assertEquals(1, result.size());
        verify(authorService, times(1)).findAll();
    }

    @Test
    public void testGetAuthorById() {

        when(authorService.findById(1)).thenReturn(authorDTO);

        AuthorDTO result = authorController.getAuthor(1);

        assertNotNull(result);
        assertEquals("Author Name", result.getName());
        verify(authorService, times(1)).findById(1);
    }


    @Test
    public void testSaveAuthor() {
        authorController.saveAuthor(author);

        verify(authorService, times(1)).save(any(Author.class));
    }


    @Test
    public void testUpdateAuthor() {

        doNothing().when(authorService).save(any(Author.class));

        Author result = authorController.updateAuthor(1, author);

        verify(authorService, times(1)).save(any(Author.class));


        assertNotNull(result);
        assertEquals("Author Name", result.getName());
        assertEquals(1, result.getId());
    }

    @Test
    public void testDeleteAuthor() {
        String response = authorController.deleteAuthor(1);

        assertEquals("Author with Id: 1 is deleted", response);
        verify(authorService, times(1)).deleteAuthor(1);
    }


    @Test
    public void testGetBooksByAuthorId() {

        Book book1 = new Book(1, 123456789, "Book One", 1);
        Book book2 = new Book(2, 987654321, "Book Two", 1);
        List<Book> books = Arrays.asList(book1, book2);


        when(authorService.getBooksByAuthorId(1)).thenReturn(books);

        List<Book> result = authorController.getBooksByAuthorId(1);


        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Book One", result.get(0).getName());
        assertEquals("Book Two", result.get(1).getName());


        verify(authorService, times(1)).getBooksByAuthorId(1);
    }

}

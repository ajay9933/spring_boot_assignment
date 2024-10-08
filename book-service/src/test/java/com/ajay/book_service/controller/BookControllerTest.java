package com.ajay.book_service.controller;

import com.ajay.book_service.model.Book;
import com.ajay.book_service.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookControllerTest {

    @InjectMocks
    private BookController bookController;

    @Mock
    private BookService bookService;

    private Book book;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        book = new Book(1, 123456789, "Book Title", 1);
    }

    @Test
    public void testGetAllBooks() {
        List<Book> books = Arrays.asList(book);
        when(bookService.findAll()).thenReturn(books);

        List<Book> result = bookController.getAllBooks();

        assertEquals(1, result.size());
        verify(bookService, times(1)).findAll();
    }

    @Test
    public void testGetBookById() {
        when(bookService.findById(1)).thenReturn(book);

        Book result = bookController.getBook(1);

        assertNotNull(result);
        assertEquals("Book Title", result.getName());
        verify(bookService, times(1)).findById(1);
    }

    @Test
    public void testSaveBook() {
        book.setId(0);
        doNothing().when(bookService).save(any(Book.class));

        bookController.saveBook(book);

        verify(bookService, times(1)).save(any(Book.class));
    }


    @Test
    public void testUpdateBook() {
        when(bookService.updateBook(anyInt(), any(Book.class))).thenReturn(book);

        Book result = bookController.updateBook(1, book);

        assertNotNull(result);
        assertEquals("Book Title", result.getName());
        verify(bookService, times(1)).updateBook(1, book);
    }

    @Test
    public void testDeleteBook() {
        String response = bookController.deleteBook(1);

        assertEquals("Book with Id: 1 is deleted", response);
        verify(bookService, times(1)).deleteBook(1);
    }

    @Test
    public void testGetBooksByAuthorId() {
        List<Book> books = Arrays.asList(book);
        when(bookService.getBooksByAuthorId(1)).thenReturn(books);

        List<Book> result = bookController.getBooksByAuthorId(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Book Title", result.get(0).getName());

        verify(bookService, times(1)).getBooksByAuthorId(1);
    }
}



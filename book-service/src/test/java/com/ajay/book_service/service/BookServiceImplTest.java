package com.ajay.book_service.service;

import com.ajay.book_service.exceptions.BookExistsException;
import com.ajay.book_service.exceptions.BookNotFoundException;
import com.ajay.book_service.model.Book;
import com.ajay.book_service.repository.BookRepository;
import com.ajay.book_service.service.BookServiceImpl;
import com.ajay.book_service.utils.Constants;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceImplTest {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;

    private Book book;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        book = new Book(1, 123456, "Book Title", 1);
    }

    @Test
   public void testFindAllBooks() {
        List<Book> books = Arrays.asList(book);
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.findAll();

        assertEquals(1, result.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
   public  void testFindBookById_Success() {
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));

        Book result = bookService.findById(1);

        assertNotNull(result);
        assertEquals("Book Title", result.getName());
        verify(bookRepository, times(1)).findById(1);
    }

    @Test(expected = BookNotFoundException.class)
    public void testFindBookById_NotFound() {
        when(bookRepository.findById(1)).thenReturn(Optional.empty());

        bookService.findById(1); // This should throw an exception
    }

    @Test
    public void testSaveBook_Success() {
        when(bookRepository.findByName(book.getName())).thenReturn(Optional.empty());

        bookService.save(book);

        verify(bookRepository, times(1)).save(book);
    }

    @Test(expected = BookExistsException.class)
    public void testSaveBook_AlreadyExists() {
        when(bookRepository.findByName(book.getName())).thenReturn(Optional.of(book));

        bookService.save(book); // This should throw an exception
    }

    @Test
    public void testUpdateBook_Success() {
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));

        Book updatedBook = new Book();
        updatedBook.setName("Updated Book Title");

        Book result = bookService.updateBook(1, updatedBook);

        assertEquals("Updated Book Title", result.getName());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    public void testUpdateBook_NoNameChange() {
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));

        Book updatedBook = new Book();
        updatedBook.setName(null);  // Name remains unchanged

        Book result = bookService.updateBook(1, updatedBook);

        assertEquals("Book Title", result.getName());  // Name should remain the same
        verify(bookRepository, times(1)).save(book);
    }

    @Test(expected = BookNotFoundException.class)
    public void testUpdateBook_NotFound() {
        when(bookRepository.findById(1)).thenReturn(Optional.empty());

        Book updatedBook = new Book();
        updatedBook.setName("Updated Book Title");

        bookService.updateBook(1, updatedBook); // This should throw an exception
    }

    @Test
    public void testDeleteBook_Success() {
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));

        bookService.deleteBook(1);

        verify(bookRepository, times(1)).deleteById(1);
    }

    @Test(expected = BookNotFoundException.class)
    public void testDeleteBook_NotFound() {
        when(bookRepository.findById(1)).thenReturn(Optional.empty());

        bookService.deleteBook(1); // This should throw an exception
    }

    @Test
    public void testGetBooksByAuthorId_Success() {
        List<Book> books = Arrays.asList(book);
        when(bookRepository.findBooksByAuthorId(1)).thenReturn(books);

        List<Book> result = bookService.getBooksByAuthorId(1);

        assertEquals(1, result.size());
        verify(bookRepository, times(1)).findBooksByAuthorId(1);
    }
}


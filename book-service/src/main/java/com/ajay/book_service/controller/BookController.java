package com.ajay.book_service.controller;

import com.ajay.book_service.model.Book;
import com.ajay.book_service.service.BookService;
import com.ajay.book_service.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constants.BOOK)
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @GetMapping(Constants.EMPTY)
    public List<Book> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping(Constants.BOOK_ID)
    public Book getBook(@PathVariable int bookId) {
        return bookService.findById(bookId);
    }

    @PostMapping(Constants.EMPTY)
    public Book saveBook(@RequestBody Book book) {
        book.setId(0);
        bookService.save(book);
        return book;
    }

    @PutMapping(Constants.BOOK_ID)
    public Book updateBook(@PathVariable("bookId") int bookId,@RequestBody Book book) {
        bookService.updateBook(bookId,book);
        return book;
    }

    @DeleteMapping(Constants.BOOK_ID)
    public String deleteBook(@PathVariable int bookId) {
        bookService.deleteBook(bookId);
        return "Book with Id: " + bookId + " is deleted";
    }

    @GetMapping("/author/{authorId}")
    public List<Book> getBooksByAuthorId(@PathVariable int authorId) {
        return bookService.getBooksByAuthorId(authorId);
    }
}

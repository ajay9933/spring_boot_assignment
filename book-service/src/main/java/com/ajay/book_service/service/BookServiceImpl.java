package com.ajay.book_service.service;

import com.ajay.book_service.exceptions.BookExistsException;
import com.ajay.book_service.exceptions.BookNotFoundException;
import com.ajay.book_service.model.Book;
import com.ajay.book_service.repository.BookRepository;
import com.ajay.book_service.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override

    public Book findById(int bookId) {
        Book book=bookRepository.findById(bookId).orElseThrow(()->new BookNotFoundException(Constants.BOOK_NOT_FOUND));

        return book;
    }


    @Override
    public void save(Book book) {



        Optional<Book> authorExists= bookRepository.findByName(book.getName());
        if(authorExists.isPresent()) {
            throw new BookExistsException(Constants.BOOK_EXISTS);
        }
        bookRepository.save(book);
    }

    @Override
    public void deleteBook(int bookId) {
        Book book=bookRepository.findById(bookId).orElseThrow(()->new BookNotFoundException(Constants.BOOK_NOT_FOUND));

        bookRepository.deleteById(bookId);
    }

    @Override
    public Book updateBook(int bookId, Book book) {
        Book oldBook = bookRepository.findById(bookId).orElseThrow(()->new BookNotFoundException(Constants.BOOK_NOT_FOUND));
        if(book.getName()!=null)
        {
            oldBook.setName(book.getName());
        }
        bookRepository.save(oldBook);
        return oldBook;
    }

    @Override
    public List<Book> getBooksByAuthorId(int authorId) {
        return bookRepository.findBooksByAuthorId(authorId);
    }
}

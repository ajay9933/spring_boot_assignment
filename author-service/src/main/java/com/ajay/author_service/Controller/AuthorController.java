package com.ajay.author_service.Controller;

import com.ajay.author_service.dto.AuthorDTO;
import com.ajay.author_service.model.Author;
import com.ajay.author_service.model.Book;
import com.ajay.author_service.services.AuthorService;
import com.ajay.author_service.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(Constants.AUTHOR)
public class AuthorController {


    private final AuthorService authorService;

   @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping(Constants.EMPTY)
    public List<AuthorDTO> getAuthors() {
        return authorService.findAll();
    }

    @GetMapping(Constants.AUTHOR_ID)
    public AuthorDTO getAuthor(@PathVariable int authorId) {
        return authorService.findById(authorId);
    }

    @PostMapping(Constants.EMPTY)
    public Author saveAuthor(@RequestBody Author author) {
        author.setId(0);
        authorService.save(author);
        return author;
    }

    @PutMapping(Constants.AUTHOR_ID)
    public Author updateAuthor(@PathVariable("authorId") int authorId, @RequestBody Author author) {
        authorService.save(author);
        return author;
    }

    @DeleteMapping(Constants.AUTHOR_ID)
    public String deleteAuthor(@PathVariable int authorId) {
        authorService.deleteAuthor(authorId);
        return "Author with Id: " + authorId + " is deleted";
    }

    @GetMapping("/{authorId}/books")
    public List<Book> getBooksByAuthorId(@PathVariable int authorId) {
        return authorService.getBooksByAuthorId(authorId);
    }
}

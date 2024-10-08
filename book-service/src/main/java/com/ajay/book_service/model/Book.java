package com.ajay.book_service.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "books")
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "isbn")
    private int isbn;

    @Column(name = "name")
    private String name;

    @Column(name = "authorId")
    private int authorId;


    public Book(int id, int isbn, String name, int authorId) {
        this.id = id;
        this.isbn = isbn;
        this.name = name;
        this.authorId = authorId;
    }

    public Book() {
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn=" + isbn +
                ", name='" + name + '\'' +
                '}';
    }
}

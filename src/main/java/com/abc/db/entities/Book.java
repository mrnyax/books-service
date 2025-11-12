package com.abc.db.entities;

import com.abc.dtos.BookDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Book author is required")
    private String title;
    @NotNull(message = "Book author is required")
    private String author;
    private int year;

    public Book() {
    }

    public Book(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public static Book of(final BookDto bookDtoRequest) {
        return new Book(bookDtoRequest.title(), bookDtoRequest.author(), bookDtoRequest.year());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}

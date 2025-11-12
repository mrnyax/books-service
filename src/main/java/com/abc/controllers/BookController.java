package com.abc.controllers;

import com.abc.dtos.BookDto;
import com.abc.services.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(final BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<BookDto> addBook(@RequestBody @Valid BookDto bookDtoRequest) {
        BookDto createdBook = bookService.save(bookDtoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> bookList = bookService.getBookList();
        return ResponseEntity.ok(bookList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        BookDto book = bookService.getById(id);
        return ResponseEntity.ok(book);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @RequestBody BookDto bookDtoRequest) {
        BookDto book = bookService.updateBook(id, bookDtoRequest);
        return ResponseEntity.ok(book);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BookDto> deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

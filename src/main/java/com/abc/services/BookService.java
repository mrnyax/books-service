package com.abc.services;

import com.abc.db.entities.Book;
import com.abc.db.repositories.BookRepository;
import com.abc.dtos.BookDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(final BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookDto save(@Valid BookDto bookDtoRequest) {
        Book newBook = toBook(bookDtoRequest);
        Book savedBook = bookRepository.save(newBook);
        return toBookDto(savedBook);
    }

    public BookDto getById(Long id) {
        return bookRepository.findById(id)
                .map(this::toBookDto)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found.".formatted(id)));
    }

    public List<BookDto> getBookList() {
        return bookRepository.findAll().stream().map(this::toBookDto).toList();
    }

    public BookDto updateBook(Long id, BookDto bookDtoRequest) {
        Book bookToUpdate = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found.".formatted(id)));
        bookToUpdate.setTitle(bookDtoRequest.title());
        bookToUpdate.setAuthor(bookDtoRequest.author());
        bookToUpdate.setYear(bookDtoRequest.year());

        Book updatedBook = bookRepository.save(bookToUpdate);
        return toBookDto(updatedBook);
    }

    public void deleteById(Long id) {
        Book bookToDelete = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found.".formatted(id)));
        bookRepository.deleteById(bookToDelete.getId());
    }

    // Helper methods
    private static Book toBook(BookDto bookDtoRequest) {
        return Book.of(bookDtoRequest);
    }

    private BookDto toBookDto(Book savedBook) {
        return new BookDto(savedBook.getTitle(), savedBook.getAuthor(), savedBook.getYear());
    }
}

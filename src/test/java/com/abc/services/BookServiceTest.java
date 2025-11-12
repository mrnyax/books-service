package com.abc.services;

import com.abc.db.entities.Book;
import com.abc.db.repositories.BookRepository;
import com.abc.dtos.BookDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;


    @Test
    void save_ShouldReturnSavedBookDto() {
        // Arrange
        BookDto request = new BookDto("Title", "Author", 2020);
        Book saved = new Book("Title", "Author", 2020);

        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            Book arg = invocation.getArgument(0);
            // simulate DB assigning id
            arg.setId(saved.getId());
            return arg;
        });

        // Act
        BookDto result = bookService.save(request);

        // Assert
        assertNotNull(result);
        assertEquals("Title", result.title());
        assertEquals("Author", result.author());
        assertEquals(2020, result.year());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void getById_WhenFound_ShouldReturnBookDto() {
        // Arrange
        Book stored = new Book("Found", "Some Author", 2010);
        when(bookRepository.findById(2L)).thenReturn(Optional.of(stored));

        // Act
        var dto = bookService.getById(2L);

        // Assert
        assertEquals("Found", dto.title());
        assertEquals("Some Author", dto.author());
        assertEquals(2010, dto.year());
        verify(bookRepository, times(1)).findById(2L);
    }

    @Test
    void getById_WhenNotFound_ShouldThrowEntityNotFoundException() {
        // Arrange
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> bookService.getById(99L));
        assertTrue(ex.getMessage().contains("99"));
        verify(bookRepository, times(1)).findById(99L);
    }

    @Test
    void getBookList_ShouldReturnMappedList() {
        // Arrange
        Book a = new Book("A", "Author A", 2001);
        Book b = new Book("B", "Author B", 2002);
        when(bookRepository.findAll()).thenReturn(List.of(a, b));

        // Act
        List<BookDto> dtos = bookService.getBookList();

        // Assert
        assertThat(dtos).hasSize(2);
        assertThat(dtos).extracting(BookDto::title).containsExactly("A", "B");
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void updateBook_WhenFound_ShouldUpdateAndReturnDto() {
        // Arrange
        Long id = 5L;
        Book existing = new Book("Old", "Old Author", 1999);
        when(bookRepository.findById(id)).thenReturn(Optional.of(existing));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookDto updateRequest = new BookDto("New Title", "New Author", 2021);

        // Act
        var result = bookService.updateBook(id, updateRequest);

        // Assert
        assertEquals("New Title", result.title());
        assertEquals("New Author", result.author());
        assertEquals(2021, result.year());
        verify(bookRepository, times(1)).findById(id);
        verify(bookRepository, times(1)).save(existing);
    }

    @Test
    void updateBook_WhenNotFound_ShouldThrowEntityNotFoundException() {
        // Arrange
        Long id = 10L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> bookService.updateBook(id, new BookDto("T","A",2000)));
        assertTrue(ex.getMessage().contains(String.valueOf(id)));
        verify(bookRepository, times(1)).findById(id);
        verify(bookRepository, never()).save(any());
    }

    @Test
    void deleteById_WhenFound_ShouldCallRepositoryDelete() {
        // Arrange
        Long id = 7L;
        Book toDelete = new Book("X", "Y", 1990);
        when(bookRepository.findById(id)).thenReturn(Optional.of(toDelete));

        // Act
        bookService.deleteById(id);

        // Assert
        verify(bookRepository, times(1)).findById(id);
    }

    @Test
    void deleteById_WhenNotFound_ShouldThrowEntityNotFoundException() {
        // Arrange
        Long id = 20L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> bookService.deleteById(id));
        assertTrue(ex.getMessage().contains(String.valueOf(id)));
        verify(bookRepository, times(1)).findById(id);
        verify(bookRepository, never()).deleteById(any());
    }

}
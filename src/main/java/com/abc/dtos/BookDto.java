package com.abc.dtos;

import jakarta.validation.constraints.NotNull;

public record BookDto(
        @NotNull(message = "Book title is required")
        String title,
        @NotNull(message = "Book author is required")
        String author,
        int year
) {
}

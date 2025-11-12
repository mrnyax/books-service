package com.abc.controllers;

import com.abc.dtos.BookDto;
import com.abc.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@WithMockUser
class BookControllerTest {

    public static final String BOOKS_ENDPOINT_URL = "/books";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookService bookService;

    @Test
    void addBook_whenValidRequest_returnsStatusCreated() throws Exception {
        final BookDto bookDto = new BookDto("Book Title", "Author Test", 2015);
        when(bookService.save(any())).thenReturn(bookDto);

        mockMvc.perform(
                        post(BOOKS_ENDPOINT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(bookDto))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    void addBook_whenInvalidRequest_returnsStatusBadRequest() throws Exception {
        final BookDto bookDto = new BookDto(null, null, 2015);

        mockMvc.perform(
                        post(BOOKS_ENDPOINT_URL)
                                .with(httpBasic("admin", "password"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(bookDto))
                )
                .andExpect(status().isBadRequest());
    }

}
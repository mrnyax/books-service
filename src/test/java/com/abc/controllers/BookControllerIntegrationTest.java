package com.abc.controllers;


import com.abc.db.entities.Book;
import com.abc.db.repositories.BookRepository;
import com.abc.dtos.BookDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.List;

import static com.abc.controllers.BookControllerTest.BOOKS_ENDPOINT_URL;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.bouncycastle.cms.RecipientId.password;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerIntegrationTest extends PostgresContainerConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createGetUpdateDeleteFlow_shouldWork() throws Exception {
        final BookDto bookDto = new BookDto("Book Title", "Author Test", 2015);

        mockMvc.perform(
                        post(BOOKS_ENDPOINT_URL)
                                .with(httpBasic("admin", "password"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(bookDto))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Book Title"))
                .andExpect(jsonPath("$.author").value("Author Test"))
                .andExpect(jsonPath("$.year").value(2015));

        // verify persistedBooks and get its id
        List<Book> persistedBooks = bookRepository.findAll();
        assertThat(persistedBooks).hasSize(1);
        Long id = persistedBooks.get(0).getId();
        assertThat(id).isNotNull();
    }

}
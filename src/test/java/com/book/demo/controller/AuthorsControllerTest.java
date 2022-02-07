package com.book.demo.controller;

import com.book.demo.domain.Book;
import com.book.demo.domain.BookStatus;
import com.book.demo.persistance.BooksRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorsController.class)
public class AuthorsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BooksRepository repositoryMock;

    @Test
    public void getAuthors() throws Exception {
        Book book1 = new Book(
                UUID.randomUUID().toString(),
                "Marin Preda",
                "Cel mai iubit dintre pamanteni",
                1980,
                "Realism",
                BookStatus.TO_READ.getStatus()
        );

        Book book2 = new Book(
                UUID.randomUUID().toString(),
                "Marin Preda",
                "Morometii",
                1955,
                "Realism",
                BookStatus.READING.getStatus()
        );

        Book book3 = new Book(
                UUID.randomUUID().toString(),
                "Mihai Eminescu",
                "Poesii",
                1883,
                "Poetry",
                BookStatus.TO_READ.getStatus()
        );

        Mockito.when(repositoryMock.findAll()).thenReturn(List.of(book1, book2, book3));

        mockMvc.perform(get("/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", Matchers.is("Marin Preda")));
    }
}

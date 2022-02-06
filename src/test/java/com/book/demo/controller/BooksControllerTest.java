package com.book.demo.controller;

import com.book.demo.domain.Book;
import com.book.demo.domain.BookStatus;
import com.book.demo.persistance.BooksRepository;
import com.book.demo.service.OperationLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BooksController.class)
public class BooksControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BooksRepository repositoryMock;

    @MockBean
    private OperationLogService operationLogService;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testGetBookById() throws Exception {
        Book book = new Book(
                UUID.randomUUID().toString(),
                "Marin Preda",
                "Cel mai iubit dintre pamanteni",
                1980,
                "Realism",
                BookStatus.TO_READ.getStatus()
        );

        Mockito.when(repositoryMock.findById(Mockito.any())).thenReturn(Optional.of(book));

        mockMvc.perform(get("/books/" + book.getBookID())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }
}

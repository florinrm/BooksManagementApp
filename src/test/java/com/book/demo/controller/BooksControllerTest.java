package com.book.demo.controller;

import com.book.demo.domain.Book;
import com.book.demo.domain.BookStatus;
import com.book.demo.domain.OperationLog;
import com.book.demo.domain.OperationType;
import com.book.demo.persistance.BooksRepository;
import com.book.demo.service.OperationLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.book.demo.service.Keys.PROCESSING_QUEUE;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BooksController.class)
public class BooksControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BooksRepository repositoryMock;

    @MockBean
    private OperationLogService operationLogService;

    @Autowired
    private ObjectMapper mapper;

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
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.author", Matchers.is("Marin Preda")));
    }

    @Test
    public void testGetBookByIdReturnsNotFound() throws Exception {
        Mockito.when(repositoryMock.findById(Mockito.any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/books/" + "some_id")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetBooks() throws Exception {
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

        Mockito.when(repositoryMock.findAll()).thenReturn(List.of(book1, book2));

        mockMvc.perform(get("/books/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", Matchers.is("Cel mai iubit dintre pamanteni")))
                .andExpect(jsonPath("$[1].title", Matchers.is("Morometii")));
    }

    @Test
    public void testGetBooksByYear() throws Exception {
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

        Mockito.when(repositoryMock.findAll()).thenReturn(List.of(book1, book2));

        mockMvc.perform(get("/books?year=1980")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", Matchers.is("Cel mai iubit dintre pamanteni")));
    }

    @Test
    public void testGetBooksByGenre() throws Exception {
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

        mockMvc.perform(get("/books?genre=Poetry")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", Matchers.is("Poesii")));
    }

    @Test
    public void testAddBook() throws Exception {
        Book book = new Book(
                UUID.randomUUID().toString(),
                "Marin Preda",
                "Cel mai iubit dintre pamanteni",
                1980,
                "Realism",
                BookStatus.TO_READ.getStatus()
        );

        Mockito.when(repositoryMock.save(Mockito.any())).thenReturn(book);
        var jsonBook = mapper.writeValueAsString(book);

        mockMvc.perform(post("/books/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBook)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.author", Matchers.is("Marin Preda")));

        verify(operationLogService, times(1)).logOperation(OperationType.ADD_BOOK);
    }

    @Test
    public void testAddDuplicateBook() throws Exception {
        Book book = new Book(
                UUID.randomUUID().toString(),
                "Marin Preda",
                "Cel mai iubit dintre pamanteni",
                1980,
                "Realism",
                BookStatus.TO_READ.getStatus()
        );

        Mockito.when(repositoryMock.save(Mockito.any())).thenReturn(book);
        var jsonBook = mapper.writeValueAsString(book);

        mockMvc.perform(post("/books/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBook)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.author", Matchers.is("Marin Preda")));

        verify(operationLogService, times(1)).logOperation(OperationType.ADD_BOOK);

        Mockito.when(repositoryMock.existsById(Mockito.any())).thenReturn(true);
        mockMvc.perform(post("/books/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBook)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void testAddBookWithWrongStatus() throws Exception {
        Book book = new Book(
                UUID.randomUUID().toString(),
                "Marin Preda",
                "Cel mai iubit dintre pamanteni",
                1980,
                "Realism",
                "some_wrong_status"
        );

        Mockito.when(repositoryMock.save(Mockito.any())).thenReturn(book);
        var jsonBook = mapper.writeValueAsString(book);

        mockMvc.perform(post("/books/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBook)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteBook() throws Exception {
        String id = UUID.randomUUID().toString();

        Mockito.when(repositoryMock.existsById(Mockito.any())).thenReturn(true);

        mockMvc.perform(delete("/books/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(operationLogService, times(1)).logOperation(OperationType.DELETE_BOOK);
    }

    @Test
    public void testDeleteBookNotFound() throws Exception {
        String id = UUID.randomUUID().toString();

        Mockito.when(repositoryMock.existsById(Mockito.any())).thenReturn(false);

        mockMvc.perform(delete("/books/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateBook() throws Exception {
        String id = UUID.randomUUID().toString();
        Book book = new Book(
                UUID.randomUUID().toString(),
                "Marin Preda",
                "Cel mai iubit dintre pamanteni",
                1980,
                "Realism",
                BookStatus.TO_READ.getStatus()
        );

        Mockito.when(repositoryMock.existsById(Mockito.any())).thenReturn(true);
        Mockito.when(repositoryMock.save(Mockito.any())).thenReturn(book);
        var jsonBook = mapper.writeValueAsString(book);

        mockMvc.perform(put("/books/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBook)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.author", Matchers.is("Marin Preda")));

        verify(repositoryMock, times(1)).deleteById(id);
        verify(operationLogService, times(1)).logOperation(OperationType.UPDATE_BOOK);
    }

    @Test
    public void testUpdateBookWrongId() throws Exception {
        String id = UUID.randomUUID().toString();
        Book book = new Book(
                UUID.randomUUID().toString(),
                "Marin Preda",
                "Cel mai iubit dintre pamanteni",
                1980,
                "Realism",
                BookStatus.TO_READ.getStatus()
        );

        Mockito.when(repositoryMock.existsById(Mockito.any())).thenReturn(false);
        var jsonBook = mapper.writeValueAsString(book);

        mockMvc.perform(put("/books/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBook)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getReadBooks() throws Exception {
        Book book1 = new Book(
                UUID.randomUUID().toString(),
                "Marin Preda",
                "Cel mai iubit dintre pamanteni",
                1980,
                "Realism",
                BookStatus.READ.getStatus()
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

        mockMvc.perform(get("/books/read")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", Matchers.is("Cel mai iubit dintre pamanteni")));
    }

    @Test
    public void getToReadBooks() throws Exception {
        Book book1 = new Book(
                UUID.randomUUID().toString(),
                "Marin Preda",
                "Cel mai iubit dintre pamanteni",
                1980,
                "Realism",
                BookStatus.READ.getStatus()
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

        mockMvc.perform(get("/books/to-read")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", Matchers.is("Poesii")));
    }

    @Test
    public void getReadingBooks() throws Exception {
        Book book1 = new Book(
                UUID.randomUUID().toString(),
                "Marin Preda",
                "Cel mai iubit dintre pamanteni",
                1980,
                "Realism",
                BookStatus.READ.getStatus()
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

        mockMvc.perform(get("/books/reading")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", Matchers.is("Morometii")));
    }
}

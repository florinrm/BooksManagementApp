package com.book.demo.controller;

import com.book.demo.domain.OperationLog;
import com.book.demo.domain.OperationType;
import com.book.demo.persistance.BooksRepository;
import com.book.demo.persistance.OperationLogRepository;
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

@WebMvcTest(LogsController.class)
public class LogsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OperationLogRepository repositoryMock;

    @Test
    public void getLogs() throws Exception {
        var log1 = new OperationLog(UUID.randomUUID().toString(), OperationType.DELETE_BOOK.name(), "time1");
        var log2 = new OperationLog(UUID.randomUUID().toString(), OperationType.UPDATE_BOOK.name(), "time2");
        var log3 = new OperationLog(UUID.randomUUID().toString(), OperationType.ADD_BOOK.name(), "time3");
        Mockito.when(repositoryMock.findAll()).thenReturn(List.of(log1, log2, log3));

        mockMvc.perform(get("/logs/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$", hasSize(3)));
    }
}

package com.book.demo.controller;

import com.book.demo.domain.Book;
import com.book.demo.domain.OperationLog;
import com.book.demo.persistance.OperationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/logs")
public class LogsController {
    @Autowired
    private OperationLogRepository operationLogRepository;

    @GetMapping
    public List<OperationLog> getLogs() {
        var fetchedResults = operationLogRepository.findAll();
        List<OperationLog> logs = new ArrayList<>();
        fetchedResults.forEach(logs::add);

        return logs;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OperationLog> getLogById(@PathVariable String id) {
        var result = operationLogRepository.findById(id);
        return result.map(book -> new ResponseEntity<>(book, HttpStatus.OK)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}

package com.book.demo.controller;

import com.book.demo.domain.OperationLog;
import com.book.demo.persistance.OperationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/controller")
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
}

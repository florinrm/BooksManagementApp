package com.book.demo.service;

import com.book.demo.domain.OperationLog;
import com.book.demo.domain.OperationType;
import com.book.demo.persistance.OperationLogRepository;
import com.book.demo.service.workers.OperationLogTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class OperationLogService {
    @Autowired
    private OperationLogRepository operationLogRepository;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public void logOperation(OperationType type) {
        OperationLog operationLog = new OperationLog(
                UUID.randomUUID().toString(),
                type.name(),
                LocalDate.now().toString()
        );
        System.out.println(operationLog);
        executorService.submit(new OperationLogTask(operationLogRepository, operationLog));
    }
}

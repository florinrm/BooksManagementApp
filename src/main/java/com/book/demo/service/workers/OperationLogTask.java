package com.book.demo.service.workers;

import com.book.demo.domain.OperationLog;
import com.book.demo.persistance.OperationLogRepository;
import lombok.SneakyThrows;

public class OperationLogTask implements Runnable {
    private final OperationLogRepository operationLogRepository;
    private final OperationLog log;

    public OperationLogTask(
            OperationLogRepository operationLogRepository,
            OperationLog log
    ) {
        this.operationLogRepository = operationLogRepository;
        this.log = log;
    }

    @SneakyThrows
    @Override
    public void run() {
        System.out.println("Log taken from queue: " + log);
        operationLogRepository.save(log);
    }
}

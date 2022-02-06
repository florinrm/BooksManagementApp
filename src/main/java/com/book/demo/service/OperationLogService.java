package com.book.demo.service;

import com.book.demo.domain.OperationLog;
import com.book.demo.domain.OperationType;
import com.book.demo.persistance.OperationLogRepository;
import com.book.demo.service.workers.OperationLogTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.book.demo.service.Keys.PROCESSING_QUEUE;

@Service
public class OperationLogService {
    @Autowired
    private OperationLogRepository operationLogRepository;

    @Autowired
    private RedisTemplate<String, OperationLog> queue;

    @Async
    public void logOperation(OperationType type) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        OperationLog operationLog = new OperationLog(
                UUID.randomUUID().toString(),
                type.name(),
                LocalDate.now().toString()
        );
        queue.opsForList().rightPush(PROCESSING_QUEUE, operationLog);
        executorService.submit(new OperationLogTask(executorService, operationLogRepository, queue, operationLog));
    }
}

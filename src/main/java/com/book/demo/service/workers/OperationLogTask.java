package com.book.demo.service.workers;

import com.book.demo.exceptions.EmptyQueueException;
import com.book.demo.domain.OperationLog;
import com.book.demo.persistance.OperationLogRepository;
import com.book.demo.service.Keys;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.ExecutorService;

public class OperationLogTask implements Runnable {
    private final ExecutorService executorService;
    private final OperationLogRepository operationLogRepository;
    private final RedisTemplate<String, OperationLog> queue;

    public OperationLogTask(
            ExecutorService executorService, OperationLogRepository operationLogRepository,
            RedisTemplate<String, OperationLog> queue
    ) {
        this.executorService = executorService;
        this.operationLogRepository = operationLogRepository;
        this.queue = queue;
    }

    @SneakyThrows
    @Override
    public void run() {
        if (queue.opsForList().size(Keys.PROCESSING_QUEUE) == null
                || queue.opsForList().size(Keys.PROCESSING_QUEUE) == 0) {
            executorService.shutdown();
        }

        OperationLog log = queue.opsForList().leftPop(Keys.PROCESSING_QUEUE);
        if (log == null) {
            throw new EmptyQueueException();
        }
        operationLogRepository.save(log);

        if (queue.opsForList().size(Keys.PROCESSING_QUEUE) == null
                || queue.opsForList().size(Keys.PROCESSING_QUEUE) == 0) {
            executorService.shutdown();
        }
    }
}

package com.book.demo.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@RedisHash("operation-log")
public class OperationLog {
    private @Id
    @NonNull String logID;
    private @NonNull String operationType;
    private @NonNull String timestamp;
}

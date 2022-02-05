package com.book.demo.domain;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@RedisHash("task")
public class BooksWordsProcessingTask {
    private @Indexed
    @NonNull List<String> titles;
    private @Indexed
    @NonNull List<String> words;
    private @Indexed
    @NonNull String status;
}

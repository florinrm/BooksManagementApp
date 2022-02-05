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
@RedisHash("author")
public class Author {
    private @Indexed
    @NonNull String name;
    private @Indexed
    @NonNull List<String> books;
}

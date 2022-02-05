package com.book.demo.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@RedisHash("book")
public class Book {
    private @Id
    @NonNull String bookID;
    private @Indexed
    @NonNull String author;
    private @Indexed
    @NonNull String title;
    private @Indexed
    @NonNull int year;
    private @Indexed
    @NonNull String genre;
    private @Indexed
    @NonNull String bookStatus;
}

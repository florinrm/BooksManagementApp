package com.book.demo.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@RedisHash("book")
public class Book {
    private @Id
    @NonNull String bookID;
    private @Indexed
    @NonNull String author;
    private @Indexed
    @NonNull String title;
    private @Indexed int year;
    private @Indexed
    @NonNull String genre;
    private @Indexed
    @NonNull String bookStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return year == book.year &&
                Objects.equals(bookID, book.bookID) &&
                Objects.equals(author, book.author) &&
                Objects.equals(title, book.title) &&
                Objects.equals(genre, book.genre) &&
                Objects.equals(bookStatus, book.bookStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookID, author, title, year, genre, bookStatus);
    }
}

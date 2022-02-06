package com.book.demo.persistance;

import com.book.demo.domain.Book;
import org.springframework.data.repository.CrudRepository;

public interface BooksRepository extends CrudRepository<Book, String> {
}
package com.book.demo.persistance;

import com.book.demo.domain.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public interface BooksRepository extends CrudRepository<Book, String> {
}
package com.book.demo.controller;

import com.book.demo.domain.Book;
import com.book.demo.domain.BookStatus;
import com.book.demo.persistance.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
public class BooksController {
    @Autowired
    private BooksRepository booksRepository;

    @GetMapping
    public List<Book> getBooks(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String year
    ) {
        List<Book> books = new ArrayList<>();
        var result = booksRepository.findAll();
        result.forEach(books::add);

        if (genre != null) {
            books = books.stream().filter(book -> book.getGenre().equals(genre)).collect(Collectors.toList());
        }

        if (year != null) {
            books = books.stream().filter(book -> book.getYear() == Integer.parseInt(year)).collect(Collectors.toList());
        }

        return books;
    }

    @PostMapping
    public ResponseEntity<Book> addBook(Book book) {
        if (!validBookStatus(book)) {
            return ResponseEntity.badRequest().build();
        }
        if (booksRepository.existsById(book.getBookID())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return new ResponseEntity<>(booksRepository.save(book), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Book> deleteBook(@PathVariable String id) {
        if (!booksRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        booksRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public boolean validBookStatus(Book book) {
        return book.getBookStatus().equals(BookStatus.READ.getStatus())
                || book.getBookStatus().equals(BookStatus.READING.getStatus())
                || book.getBookStatus().equals(BookStatus.TO_READ.getStatus());
    }
}

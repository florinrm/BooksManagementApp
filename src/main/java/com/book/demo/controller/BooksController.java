package com.book.demo.controller;

import com.book.demo.domain.Book;
import com.book.demo.domain.BookStatus;
import com.book.demo.domain.OperationType;
import com.book.demo.persistance.BooksRepository;
import com.book.demo.service.OperationLogService;
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

    @Autowired
    private OperationLogService operationLogService;

    @GetMapping
    public List<Book> getBooks(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String year
    ) {
        List<Book> books = fetchAllBooks();

        if (genre != null) {
            books = books.stream().filter(book -> book.getGenre().equals(genre)).collect(Collectors.toList());
        }

        if (year != null) {
            books = books.stream().filter(book -> book.getYear() == Integer.parseInt(year)).collect(Collectors.toList());
        }

        return books;
    }

    @GetMapping("/read")
    public List<Book> getReadBooks() {
        return filterBookByStatus(BookStatus.READ);
    }

    @GetMapping("/reading")
    public List<Book> getReadingBooks() {
        return filterBookByStatus(BookStatus.READING);
    }

    @GetMapping("/to-read")
    public List<Book> getToReadBooks() {
        return filterBookByStatus(BookStatus.TO_READ);
    }

    @PostMapping
    public ResponseEntity<?> addBook(Book book) {
        if (!validBookStatus(book)) {
            return ResponseEntity.badRequest().build();
        }
        if (booksRepository.existsById(book.getBookID())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Book with id " + book.getBookID() + " already exists!");
        }
        operationLogService.logOperation(OperationType.ADD_BOOK);
        return new ResponseEntity<>(booksRepository.save(book), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable String id) {
        if (!booksRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book with id " + id + " does not exist!");
        }
        operationLogService.logOperation(OperationType.DELETE_BOOK);
        booksRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable String id) {
        var result = booksRepository.findById(id);
        return result.map(book -> new ResponseEntity<>(book, HttpStatus.OK)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modifyBookById(@RequestBody Book book, @PathVariable String id) {
        var result = booksRepository.findById(id);
        if (result.isPresent()) {
            booksRepository.deleteById(id);
            operationLogService.logOperation(OperationType.UPDATE_BOOK);
            return new ResponseEntity<>(booksRepository.save(book), HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book with id " + id + " does not exist!");
        }
    }

    public boolean validBookStatus(Book book) {
        return book.getBookStatus() != null && (book.getBookStatus().equals(BookStatus.READ.getStatus())
                || book.getBookStatus().equals(BookStatus.READING.getStatus())
                || book.getBookStatus().equals(BookStatus.TO_READ.getStatus()));
    }

    private List<Book> fetchAllBooks() {
        List<Book> books = new ArrayList<>();
        var result = booksRepository.findAll();
        result.forEach(books::add);
        return books;
    }

    private List<Book> filterBookByStatus(BookStatus bookStatus) {
        List<Book> books = fetchAllBooks();
        return books.stream()
                .filter(book -> book.getBookStatus().equals(bookStatus.getStatus()))
                .collect(Collectors.toList());
    }
}
